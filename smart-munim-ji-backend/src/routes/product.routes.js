const express = require("express");
const ProductController = require("../controllers/product.controller");
const authenticate = require("../middleware/auth.middleware");
const restrictTo = require("../middleware/role.middleware");
const validate = require("../middleware/validation.middleware");
const { registerProductSchema } = require("../validation/product.validation");

const router = express.Router();

router.post(
  "/products/register",
  authenticate,
  restrictTo("CUSTOMER"),
  validate(registerProductSchema),
  ProductController.registerProduct
);
router.get(
  "/products/registered",
  authenticate,
  restrictTo("CUSTOMER"),
  ProductController.getRegisteredProducts
);

module.exports = router;
