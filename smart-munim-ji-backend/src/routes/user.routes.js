const express = require("express");
const UserController = require("../controllers/user.controller");
const authenticate = require("../middleware/auth.middleware");
const restrictTo = require("../middleware/role.middleware");
const validate = require("../middleware/validation.middleware");
const { updateProfileSchema } = require("../validation/user.validation");

const router = express.Router();

router.put(
  "/users/:userId",
  authenticate,
  restrictTo("CUSTOMER"),
  validate(updateProfileSchema),
  UserController.updateProfile
);

module.exports = router;
