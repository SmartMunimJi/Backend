const UserModel = require("../models/user.model");
const { ResourceNotFoundError, BaseError } = require("../errors");

class UserService {
  static async updateProfile(authUserId, userId, { name, address }) {
    if (parseInt(authUserId) !== parseInt(userId)) {
      throw new BaseError(
        "Unauthorized: Cannot update another user’s profile",
        403
      );
    }

    const user = await UserModel.findById(userId);
    if (!user) {
      throw new ResourceNotFoundError("User not found");
    }

    await UserModel.update(userId, { name, address });
  }
}

module.exports = UserService;
