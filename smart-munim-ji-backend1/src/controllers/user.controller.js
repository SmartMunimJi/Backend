const UserService = require("../services/user.service");
const SellerService = require("../services/seller.service");
const { updateProfileSchema } = require("../validation/user.validation");
const validationMiddleware = require("../middleware/validation.middleware");
const { BaseError } = require("../errors");

class UserController {
  static updateProfile = [
    validationMiddleware(updateProfileSchema),
    async (req, res, next) => {
      try {
        if (req.user.userId !== parseInt(req.params.userId)) {
          throw new BaseError(
            "Unauthorized: Cannot update another user’s profile",
            403
          );
        }
        await UserService.updateProfile(req.user.userId, req.body);
        res.status(200).json({ message: "Profile updated successfully" });
      } catch (err) {
        next(err);
      }
    },
  ];

  static getMySellers = [
    async (req, res, next) => {
      try {
        const sellers = await SellerService.getSellersByCustomerProducts(
          req.user.userId
        );
        res.status(200).json(sellers);
      } catch (err) {
        next(err);
      }
    },
  ];
}

module.exports = UserController;
