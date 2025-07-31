const express = require("express");
const AuthController = require("../controllers/auth.controller");
const validate = require("../middleware/validation.middleware");
const {
  registerCustomerSchema,
  loginSchema,
} = require("../validation/auth.validation");

const router = express.Router();

router.post(
  "/register/customer",
  validate(registerCustomerSchema),
  AuthController.registerCustomer
);
router.post("/login", validate(loginSchema), AuthController.login);

module.exports = router;
