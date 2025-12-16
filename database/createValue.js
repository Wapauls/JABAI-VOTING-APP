const sqlite3 = require("sqlite3").verbose();

const db = new sqlite3.Database("voting.db", (err) => {
    if (err) console.error(err);
    else console.log("Connected to voting.db");
});

db.serialize(() => {

    // POSITIONS
    db.run(`
        INSERT OR IGNORE INTO positions (name) VALUES
        ('President'),
        ('Vice President'),
        ('Secretary'),
        ('Treasurer'),
        ('Auditor'),
        ('Public Information Officer')
    `);

    // COURSES
    db.run(`
        INSERT OR IGNORE INTO courses (name) VALUES
        ('BSIT'),
        ('BSCS'),
        ('BSEMC'),
        ('BSIS')
    `);

    // YEARS
    db.run(`
        INSERT OR IGNORE INTO years (name) VALUES
        ('1st Year'),
        ('2nd Year'),
        ('3rd Year'),
        ('4th Year')
    `);

    // SECTIONS
    db.run(`
        INSERT OR IGNORE INTO sections (name) VALUES
        ('A'),
        ('B'),
        ('C'),
        ('D')
    `);

    console.log("Category values inserted successfully!");
});

db.close();
