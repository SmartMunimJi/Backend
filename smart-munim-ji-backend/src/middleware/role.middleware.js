const { BaseError } = require("../errors");

const restrictTo = (...roles) => {
  return (req, res, next) => {
    if (!roles.includes(req.user.role)) {
      throw new BaseError("Unauthorized: Insufficient role permissions", 403);
    }
    next();
  };
};

module.exports = restrictTo;
