const pool = require("../config/db.config");

class WarrantyClaimModel {
  static async create({
    registeredProductId,
    customerUserId,
    issueDescription,
  }) {
    const [result] = await pool.query(
      `INSERT INTO warranty_claims (registered_product_id, customer_user_id, issue_description, claim_status)
       VALUES (?, ?, ?, 'REQUESTED')`,
      [registeredProductId, customerUserId, issueDescription]
    );
    return result.insertId;
  }

  static async findById(claimId) {
    const [rows] = await pool.query(
      `SELECT wc.claim_id, wc.registered_product_id, crp.product_name, s.shop_name, wc.issue_description, wc.claim_status, wc.seller_response_notes, wc.claimed_at, wc.last_status_update_at
       FROM warranty_claims wc
       JOIN customer_registered_products crp ON wc.registered_product_id = crp.registered_product_id
       JOIN sellers s ON crp.seller_id = s.seller_id
       WHERE wc.claim_id = ?`,
      [claimId]
    );
    return rows[0];
  }

  static async findActiveByProductId(registeredProductId) {
    const [rows] = await pool.query(
      `SELECT * FROM warranty_claims WHERE registered_product_id = ? AND claim_status IN ('REQUESTED', 'IN_PROGRESS')`,
      [registeredProductId]
    );
    return rows;
  }
}

module.exports = WarrantyClaimModel;
