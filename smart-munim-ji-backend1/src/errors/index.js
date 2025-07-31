const BaseError = require("./baseError");
const ResourceNotFoundError = require("./resourceNotFoundError");
const InvalidCredentialsError = require("./invalidCredentialsError");
const ExternalAPICallFailedError = require("./externalAPICallFailedError");
const DuplicateProductRegistrationError = require("./duplicateProductRegistrationError");

module.exports = {
  BaseError,
  ResourceNotFoundError,
  InvalidCredentialsError,
  ExternalAPICallFailedError,
  DuplicateProductRegistrationError,
};
