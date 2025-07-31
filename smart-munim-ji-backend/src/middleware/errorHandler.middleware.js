const logger = require("winston");

const errorHandler = (err, req, res, next) => {
  logger.error({
    error: err.name || "Error",
    message: err.message,
    stack: err.stack,
    path: req.path,
    timestamp: new Date().toISOString(),
  });

  const statusCode = err.statusCode || 500;
  const errorResponse = {
    timestamp: new Date().toISOString(),
    status: statusCode,
    error: err.name || "InternalServerError",
    message: err.message || "An unexpected error occurred",
    path: req.path,
  };

  res.status(statusCode).json(errorResponse);
};

module.exports = errorHandler;
