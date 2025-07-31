const winston = require("winston");

const logger = winston.createLogger({
  transports: [new winston.transports.Console()],
  format: winston.format.combine(
    winston.format.timestamp(),
    winston.format.json()
  ),
});

module.exports = (err, req, res, next) => {
  let statusCode = err.statusCode || 500;
  let message = err.message || "An unexpected error occurred";

  if (err.isJoi) {
    statusCode = 400;
    message = err.details.map((d) => d.message).join(", ");
  } else if (err.code === "ER_DUP_ENTRY") {
    statusCode = 409;
    message = "Duplicate entry detected. Resource already exists.";
  }

  if (!err.isOperational || statusCode >= 500) {
    logger.error({
      error: err.name,
      message: err.message,
      stack: err.stack,
      path: req.originalUrl,
      timestamp: new Date().toISOString(),
    });
  }

  res.status(statusCode).json({
    timestamp: new Date().toISOString(),
    status: statusCode,
    error: err.name || "Internal Server Error",
    message,
    path: req.originalUrl,
  });
};
