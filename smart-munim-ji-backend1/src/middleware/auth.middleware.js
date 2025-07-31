const { verifyToken } = require("../utils/jwt.util");
const { BaseError } = require("../errors/baseError");

module.exports = (req, res, next) => {
  const authHeader = req.headers.authorization;
  if (!authHeader || !authHeader.startsWith("Bearer ")) {
    throw new BaseError("Authorization header missing or invalid", 401);
  }

  const token = authHeader.split(" ")[1];
  try {
    const decoded = verifyToken(token);
    req.user = {
      userId: decoded.userId,
      role: decoded.role,
      phoneNumber: decoded.phoneNumber,
    };
    next();
  } catch (err) {
    throw new BaseError("Invalid or expired token", 401);
  }
};
