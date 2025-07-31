const BaseError = require("./baseError");

class ResourceNotFoundError extends BaseError {
  constructor(message = "Resource not found") {
    super(message, 404);
  }
}

module.exports = ResourceNotFoundError;
