const ProductService = require("../services/product.service");
const { registerProductSchema } = require("../validation/product.validation");
const validationMiddleware = require("../middleware/validation.middleware");

class ProductController {
  static registerProduct = [
    validationMiddleware(registerProductSchema),
    async (req, res, next) => {
      try {
        const { sellerId, orderId, purchaseDate } = req.body;
        const { registeredProductId, productName, warrantyValidUntil } =
          await ProductService.registerProduct(
            req.user.userId,
            req.user.phoneNumber,
            { sellerId, orderId, purchaseDate }
          );
        res.status(201).json({
          message: "Product registered successfully",
          registeredProductId,
          productName,
          warrantyValidUntil,
        });
      } catch (err) {
        next(err);
      }
    },
  ];

  static getRegisteredProducts = [
    async (req, res, next) => {
      try {
        const products = await ProductService.getRegisteredProducts(
          req.user.userId
        );
        res.status(200).json(products);
      } catch (err) {
        next(err);
      }
    },
  ];
}

module.exports = ProductController;
