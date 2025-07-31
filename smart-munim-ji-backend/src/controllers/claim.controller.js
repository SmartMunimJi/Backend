const ClaimService = require("../services/claim.service");

class ClaimController {
  static async createClaim(req, res, next) {
    try {
      const claimId = await ClaimService.createClaim(req.user.userId, req.body);
      res.status(201).json({
        message:
          "Warranty claim submitted successfully. Seller has been notified.",
        claimId,
      });
    } catch (err) {
      next(err);
    }
  }

  static async getClaimStatus(req, res, next) {
    try {
      const claim = await ClaimService.getClaimStatus(
        req.user.userId,
        req.params.claimId
      );
      res.status(200).json(claim);
    } catch (err) {
      next(err);
    }
  }
}

module.exports = ClaimController;
