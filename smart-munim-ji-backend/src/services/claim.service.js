const CustomerRegisteredProductModel = require("../models/customerRegisteredProduct.model");
const WarrantyClaimModel = require("../models/warrantyClaim.model");
const { ResourceNotFoundError, BaseError } = require("../errors");
const moment = require("moment");

class ClaimService {
  static async createClaim(
    customerUserId,
    { registeredProductId, issueDescription }
  ) {
    const product = await CustomerRegisteredProductModel.findById(
      registeredProductId
    );
    if (!product) {
      throw new ResourceNotFoundError("Registered product not found");
    }

    if (product.customer_user_id !== customerUserId) {
      throw new BaseError("Product not owned by user", 403);
    }

    if (!moment().isSameOrBefore(product.warranty_valid_until)) {
      throw new BaseError("Product warranty has expired", 400);
    }

    const existingClaims = await WarrantyClaimModel.findActiveByProductId(
      registeredProductId
    );
    if (existingClaims.length > 0) {
      throw new BaseError(
        "An active claim already exists for this product",
        409
      );
    }

    const claimId = await WarrantyClaimModel.create({
      registeredProductId,
      customerUserId,
      issueDescription,
    });

    return claimId;
  }

  static async getClaimStatus(customerUserId, claimId) {
    const claim = await WarrantyClaimModel.findById(claimId);
    if (!claim) {
      throw new ResourceNotFoundError("Claim not found");
    }

    if (claim.customer_user_id !== customerUserId) {
      throw new BaseError("Claim not owned by user", 403);
    }

    return {
      claimId: claim.claim_id,
      registeredProductId: claim.registered_product_id,
      productName: claim.product_name,
      shopName: claim.shop_name,
      issueDescription: claim.issue_description,
      claimStatus: claim.claim_status,
      sellerResponseNotes: claim.seller_response_notes,
      claimedAt: claim.claimed_at,
      lastStatusUpdateAt: claim.last_status_update_at,
    };
  }
}

module.exports = ClaimService;
