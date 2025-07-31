const express = require("express");
const ClaimController = require("../controllers/claim.controller");
const authMiddleware = require("../middleware/auth.middleware");
const roleMiddleware = require("../middleware/role.middleware");

const router = express.Router();

router.post(
  "/",
  [authMiddleware, roleMiddleware(["CUSTOMER"])],
  ClaimController.createClaim
);
router.get(
  "/:claimId",
  [authMiddleware, roleMiddleware(["CUSTOMER"])],
  ClaimController.getClaimStatus
);

module.exports = router;
