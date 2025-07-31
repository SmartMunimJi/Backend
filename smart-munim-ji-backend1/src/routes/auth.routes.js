const express = require("express");
const AuthController = require("../controllers/auth.controller");

const router = express.Router();

router.post("/register/customer", AuthController.registerCustomer);
router.post("/login", AuthController.login);

module.exports = router;
