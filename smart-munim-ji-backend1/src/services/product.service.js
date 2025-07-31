const CustomerRegisteredProductModel = require("../models/customerRegisteredProduct.model");
const SellerService = require("./seller.service");
const {
  ResourceNotFoundError,
  ExternalAPICallFailedError,
  DuplicateProductRegistrationError,
  BaseError,
} = require("../errors");
const axios = require("axios");
const moment = require("moment");

class ProductService {
  static async registerProduct(
    customerUserId,
    customerPhoneNumber,
    { sellerId, orderId, purchaseDate }
  ) {
    const seller = await SellerService.getSellerById(sellerId);
    if (!seller.api_base_url || !seller.api_key) {
      throw new BaseError("Seller API details missing", 400);
    }

    // Validate purchase with seller's API
    const sellerApiUrl = `${seller.api_base_url}/validate-purchase`;
    try {
      const response = await axios.post(
        sellerApiUrl,
        {
          orderId,
          customerPhoneNumber,
        },
        {
          headers: { "X-API-Key": seller.api_key },
          timeout: 5000,
        }
      );

      const {
        productName,
        productPrice,
        dateOfPurchase,
        warrantyPeriodMonths,
      } = response.data;

      // Validate response data
      if (!productName || !dateOfPurchase || !warrantyPeriodMonths) {
        throw new BaseError("Invalid response from seller API", 400);
      }

      const warrantyValidUntil = moment(dateOfPurchase)
        .add(warrantyPeriodMonths, "months")
        .format("YYYY-MM-DD");

      // Store product
      try {
        const registeredProductId = await CustomerRegisteredProductModel.create(
          {
            customerUserId,
            sellerId,
            sellerOrderId: orderId,
            sellerCustomerPhoneAtSale: customerPhoneNumber,
            productName,
            productPrice,
            dateOfPurchase,
            warrantyValidUntil,
          }
        );

        return {
          registeredProductId,
          productName,
          warrantyValidUntil,
        };
      } catch (err) {
        if (err.code === "ER_DUP_ENTRY") {
          throw new DuplicateProductRegistrationError();
        }
        throw err;
      }
    } catch (err) {
      if (err.response) {
        if (err.response.status === 401) {
          throw new BaseError("Seller API authentication failed", 401);
        } else if (err.response.status === 404) {
          throw new ResourceNotFoundError("Product not found in seller system");
        } else {
          throw new ExternalAPICallFailedError();
        }
      } else if (err.request) {
        throw new ExternalAPICallFailedError("Network error or timeout");
      } else {
        throw new ExternalAPICallFailedError();
      }
    }
  }

  static async getRegisteredProducts(customerUserId) {
    return await CustomerRegisteredProductModel.findByCustomerId(
      customerUserId
    );
  }
}

module.exports = ProductService;
