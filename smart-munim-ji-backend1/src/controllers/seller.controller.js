const SellerService = require("../services/seller.service");

class SellerController {
  static getActiveSellers = [
    async (req, res, next) => {
      try {
        const sellers = await SellerService.getActiveSellers();
        res.status(200).json(sellers);
      } catch (err) {
        next(err);
      }
    },
  ];
}

module.exports = SellerController;
