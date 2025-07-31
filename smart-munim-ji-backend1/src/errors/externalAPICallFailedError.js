const BaseError = require("./baseError");

class ExternalAPICallFailedError extends BaseError {
  constructor(message = "Failed to communicate with external API") {
    super(message, 500);
  }
}

module.exports = ExternalAPICallFailedError;
