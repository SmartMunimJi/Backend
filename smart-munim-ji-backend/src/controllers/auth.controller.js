const AuthService = require("../services/auth.service");

class AuthController {
  static async registerCustomer(req, res, next) {
    try {
      const { userId, token } = await AuthService.registerCustomer(req.body);
      res.status(201).json({
        message: "Customer registered successfully. Please log in.",
        userId,
        jwtToken: token,
        role: "CUSTOMER",
      });
    } catch (err) {
      next(err);
    }
  }

  static async login(req, res, next) {
    try {
      const { userId, token, role } = await AuthService.login(req.body);
      res.status(200).json({
        message: "Login successful",
        userId,
        jwtToken: token,
        role,
      });
    } catch (err) {
      next(err);
    }
  }
}

module.exports = AuthController;
