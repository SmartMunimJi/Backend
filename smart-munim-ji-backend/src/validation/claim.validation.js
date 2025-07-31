const Joi = require("joi");

const createClaimSchema = Joi.object({
  registeredProductId: Joi.number().integer().positive().required(),
  issueDescription: Joi.string().min(10).required(),
});

module.exports = { createClaimSchema };
