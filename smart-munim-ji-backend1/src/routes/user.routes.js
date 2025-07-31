const express = require("express");
const UserController = require("../controllers/user.controller");
const authMiddleware = require("../middleware/auth.middleware");
const roleMiddleware = require("../middleware/role.middleware");

const router = express.Router();

router.put(
  "/:userId",
  [authMiddleware, roleMiddleware(["CUSTOMER"])],
  UserController.updateProfile
);
router.get(
  "/my-sellers",
  [authMiddleware, roleMiddleware(["CUSTOMER"])],
  UserController.getMySellers
);

module.exports = router;
