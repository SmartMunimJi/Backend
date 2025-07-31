const BaseError = require("./baseError");

class DuplicateProductRegistrationError extends BaseError {
  constructor(message = "Product already registered") {
    super(message, 409);
  }
}

module.exports = DuplicateProductRegistrationError;
