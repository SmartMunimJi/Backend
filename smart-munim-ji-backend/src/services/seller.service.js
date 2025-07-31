const SellerModel = require("../models/seller.model");

class SellerService {
  static async getActiveSellers() {
    const sellers = await SellerModel.findActiveSellers();
    return sellers.map((seller) => ({
      sellerId: seller.seller_id,
      shopName: seller.shop_name,
    }));
  }

  static async getSellersByCustomerProducts(customerUserId) {
    const sellers = await SellerModel.findSellersByCustomerProducts(
      customerUserId
    );
    return sellers.map((seller) => ({
      sellerId: seller.seller_id,
      shopName: seller.shop_name,
    }));
  }

  static async getSellerById(sellerId) {
    const seller = await SellerModel.findById(sellerId);
    if (!seller) {
      throw new ResourceNotFoundError("Seller not found");
    }
    return seller;
  }
}

module.exports = SellerService;
