const SellerService = require("../services/seller.service");

class SellerController {
  static async getActiveSellers(req, res, next) {
    try {
      const sellers = await SellerService.getActiveSellers();
      res.status(200).json(sellers);
    } catch (err) {
      next(err);
    }
  }

  static async getSellersByCustomerProducts(req, res, next) {
    try {
      const sellers = await SellerService.getSellersByCustomerProducts(
        req.user.userId
      );
      res.status(200).json(sellers);
    } catch (err) {
      next(err);
    }
  }
}

module.exports = SellerController;
