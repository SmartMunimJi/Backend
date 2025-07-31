const express = require("express");
const ClaimController = require("../controllers/claim.controller");
const authenticate = require("../middleware/auth.middleware");
const restrictTo = require("../middleware/role.middleware");
const validate = require("../middleware/validation.middleware");
const { createClaimSchema } = require("../validation/claim.validation");

const router = express.Router();

router.post(
  "/claims",
  authenticate,
  restrictTo("CUSTOMER"),
  validate(createClaimSchema),
  ClaimController.createClaim
);
router.get(
  "/claims/:claimId",
  authenticate,
  restrictTo("CUSTOMER"),
  ClaimController.getClaimStatus
);

module.exports = router;
