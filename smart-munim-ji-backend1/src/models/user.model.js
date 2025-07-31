const pool = require("../config/db.config");
const bcrypt = require("bcryptjs");

class UserModel {
  static async create({ name, email, password, phoneNumber, address, roleId }) {
    const passwordHash = await bcrypt.hash(password, 10);
    const [result] = await pool.query(
      `INSERT INTO users (name, email, password_hash, phone_number, address, role_id)
       VALUES (?, ?, ?, ?, ?, ?)`,
      [name, email, passwordHash, phoneNumber, address, roleId]
    );
    return result.insertId;
  }

  static async findByEmail(email) {
    const [rows] = await pool.query(`SELECT * FROM users WHERE email = ?`, [
      email,
    ]);
    return rows[0];
  }

  static async findById(userId) {
    const [rows] = await pool.query(`SELECT * FROM users WHERE user_id = ?`, [
      userId,
    ]);
    return rows[0];
  }

  static async update(userId, { name, address }) {
    await pool.query(
      `UPDATE users SET name = ?, address = ?, updated_at = CURRENT_TIMESTAMP
       WHERE user_id = ?`,
      [name, address, userId]
    );
  }
}

module.exports = UserModel;
