const jwt = require("jsonwebtoken");

const generateToken = (userId, role, phoneNumber) => {
  return jwt.sign({ userId, role, phoneNumber }, process.env.JWT_SECRET, {
    expiresIn: "1h",
  });
};

const verifyToken = (token) => {
  return jwt.verify(token, process.env.JWT_SECRET);
};

module.exports = { generateToken, verifyToken };
