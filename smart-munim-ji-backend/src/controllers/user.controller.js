const UserService = require("../services/user.service");

class UserController {
  static async updateProfile(req, res, next) {
    try {
      const { userId } = req.params;
      await UserService.updateProfile(req.user.userId, userId, req.body);
      res.status(200).json({ message: "Profile updated successfully" });
    } catch (err) {
      next(err);
    }
  }
}

module.exports = UserController;
