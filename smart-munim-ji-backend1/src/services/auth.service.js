const UserModel = require("../models/user.model");
const { generateToken } = require("../utils/jwt.util");
const {
  ResourceNotFoundError,
  InvalidCredentialsError,
  BaseError,
} = require("../errors");
const bcrypt = require("bcryptjs");
const pool = require("../config/db.config");

class AuthService {
  static async registerCustomer({
    name,
    email,
    password,
    phoneNumber,
    address,
  }) {
    const existingUser = await UserModel.findByEmail(email);
    if (existingUser) {
      throw new BaseError("Email already exists", 409);
    }

    const existingPhone = await pool.query(
      `SELECT * FROM users WHERE phone_number = ?`,
      [phoneNumber]
    );
    if (existingPhone[0].length > 0) {
      throw new BaseError("Phone number already exists", 409);
    }

    const roleId = 1; // CUSTOMER role_id
    const userId = await UserModel.create({
      name,
      email,
      password,
      phoneNumber,
      address,
      roleId,
    });

    const token = generateToken(userId, "CUSTOMER", phoneNumber);
    return { userId, token };
  }

  static async login({ email, password }) {
    const user = await UserModel.findByEmail(email);
    if (!user) {
      throw new InvalidCredentialsError();
    }

    const isMatch = await bcrypt.compare(password, user.password_hash);
    if (!isMatch) {
      throw new InvalidCredentialsError();
    }

    if (!user.is_active) {
      throw new BaseError("User account is inactive", 403);
    }

    const role =
      user.role_id === 1 ? "CUSTOMER" : user.role_id === 2 ? "SELLER" : "ADMIN";
    const token = generateToken(user.user_id, role, user.phone_number);
    return { userId: user.user_id, token, role };
  }
}

module.exports = AuthService;
