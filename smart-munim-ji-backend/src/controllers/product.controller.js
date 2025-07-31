const ProductService = require("../services/product.service");

class ProductController {
  static async registerProduct(req, res, next) {
    try {
      const { registeredProductId, productName, warrantyValidUntil } =
        await ProductService.registerProduct(
          req.user.userId,
          req.user.phoneNumber,
          req.body
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
  }

  static async getRegisteredProducts(req, res, next) {
    try {
      const products = await ProductService.getRegisteredProducts(
        req.user.userId
      );
      res.status(200).json(products);
    } catch (err) {
      next(err);
    }
  }
}

module.exports = ProductController;
