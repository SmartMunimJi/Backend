const jwt = require("jsonwebtoken");
const { BaseError } = require("../errors");

const authenticate = (req, res, next) => {
  const authHeader = req.headers.authorization;
  if (!authHeader || !authHeader.startsWith("Bearer ")) {
    throw new BaseError("Authorization header missing or invalid", 401);
  }

  const token = authHeader.split(" ")[1];
  try {
    const decoded = jwt.verify(token, process.env.JWT_SECRET);
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

module.exports = authenticate;
