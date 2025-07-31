const BaseError = require("./baseError");

class InvalidCredentialsError extends BaseError {
  constructor(message = "Invalid email or password") {
    super(message, 401);
  }
}

module.exports = InvalidCredentialsError;
