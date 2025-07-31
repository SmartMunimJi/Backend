const express = require("express");
const ProductController = require("../controllers/product.controller");
const authMiddleware = require("../middleware/auth.middleware");
const roleMiddleware = require("../middleware/role.middleware");

const router = express.Router();

router.post(
  "/register",
  [authMiddleware, roleMiddleware(["CUSTOMER"])],
  ProductController.registerProduct
);
router.get(
  "/registered",
  [authMiddleware, roleMiddleware(["CUSTOMER"])],
  ProductController.getRegisteredProducts
);

module.exports = router;
