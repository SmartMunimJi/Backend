const Joi = require("joi");

exports.claimWarrantySchema = Joi.object({
  registeredProductId: Joi.number().integer().positive().required(),
  issueDescription: Joi.string().min(10).max(1000).required(),
});
