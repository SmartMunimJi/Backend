const createPool = require("./src/config/db.config");

async function testConnection() {
  try {
    const pool = await createPool;
    const [rows] = await pool.query("SELECT 1");
    console.log("Connection successful:", rows);
    await pool.end();
  } catch (err) {
    console.error("Connection failed:", err);
  }
}

testConnection();
