const AuthService = require("../services/auth.service");
const {
  registerCustomerSchema,
  loginSchema,
} = require("../validation/auth.validation");
const validationMiddleware = require("../middleware/validation.middleware");

class AuthController {
  static registerCustomer = [
    validationMiddleware(registerCustomerSchema),
    async (req, res, next) => {
      try {
        const { name, email, password, phoneNumber, address } = req.body;
        const { userId, token } = await AuthService.registerCustomer({
          name,
          email,
          password,
          phoneNumber,
          address,
        });
        res.status(201).json({
          message: "Customer registered successfully. Please log in.",
          userId,
          jwtToken: token,
          role: "CUSTOMER",
        });
      } catch (err) {
        next(err);
      }
    },
  ];

  static login = [
    validationMiddleware(loginSchema),
    async (req, res, next) => {
      try {
        const { email, password } = req.body;
        const { userId, token, role } = await AuthService.login({
          email,
          password,
        });
        res.status(200).json({
          message: "Login successful",
          userId,
          jwtToken: token,
          role,
        });
      } catch (err) {
        next(err);
      }
    },
  ];
}

module.exports = AuthController;
