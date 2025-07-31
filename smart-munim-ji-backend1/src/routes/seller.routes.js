const express = require("express");
const SellerController = require("../controllers/seller.controller");
const authMiddleware = require("../middleware/auth.middleware");
const roleMiddleware = require("../middleware/role.middleware");

const router = express.Router();

router.get(
  "/active",
  [authMiddleware, roleMiddleware(["CUSTOMER"])],
  SellerController.getActiveSellers
);

module.exports = router;
