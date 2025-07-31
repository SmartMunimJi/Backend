const { BaseError } = require("../errors/baseError");

module.exports = (allowedRoles) => (req, res, next) => {
  if (!allowedRoles.includes(req.user.role)) {
    throw new BaseError("Forbidden: Insufficient role permissions", 403);
  }
  next();
};
