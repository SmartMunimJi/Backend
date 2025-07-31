const express = require("express");
const SellerController = require("../controllers/seller.controller");
const authenticate = require("../middleware/auth.middleware");
const restrictTo = require("../middleware/role.middleware");

const router = express.Router();

router.get(
  "/sellers/active",
  authenticate,
  restrictTo("CUSTOMER"),
  SellerController.getActiveSellers
);
router.get(
  "/customers/my-sellers",
  authenticate,
  restrictTo("CUSTOMER"),
  SellerController.getSellersByCustomerProducts
);

module.exports = router;
