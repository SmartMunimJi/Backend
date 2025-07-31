const ClaimService = require("../services/claim.service");
const { claimWarrantySchema } = require("../validation/claim.validation");
const validationMiddleware = require("../middleware/validation.middleware");

class ClaimController {
  static createClaim = [
    validationMiddleware(claimWarrantySchema),
    async (req, res, next) => {
      try {
        const { registeredProductId, issueDescription } = req.body;
        const claimId = await ClaimService.createClaim(req.user.userId, {
          registeredProductId,
          issueDescription,
        });
        res.status(201).json({
          message:
            "Warranty claim submitted successfully. Seller has been notified.",
          claimId,
        });
      } catch (err) {
        next(err);
      }
    },
  ];

  static getClaimStatus = [
    async (req, res, next) => {
      try {
        const claim = await ClaimService.getClaimStatus(
          req.user.userId,
          req.params.claimId
        );
        res.status(200).json(claim);
      } catch (err) {
        next(err);
      }
    },
  ];
}

module.exports = ClaimController;
