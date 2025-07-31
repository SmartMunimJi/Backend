const UserModel = require("../models/user.model");
const { ResourceNotFoundError, BaseError } = require("../errors");

class UserService {
  static async updateProfile(userId, { name, address }) {
    const user = await UserModel.findById(userId);
    if (!user) {
      throw new ResourceNotFoundError("User not found");
    }

    await UserModel.update(userId, { name, address });
  }
}

module.exports = UserService;
