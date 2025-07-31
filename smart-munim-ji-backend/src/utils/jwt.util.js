const jwt = require("jsonwebtoken");

const generateToken = (userId, role, phoneNumber) => {
  return jwt.sign({ userId, role, phoneNumber }, process.env.JWT_SECRET, {
    expiresIn: "1h",
  });
};

module.exports = { generateToken };
