DELETE FROM messages;
DELETE FROM chat_members;
DELETE FROM chats;
DELETE FROM comments;
DELETE FROM likes;
DELETE FROM posts;
DELETE FROM user_weekly_statistic;
DELETE FROM user_followers;
DELETE FROM users;

TRUNCATE TABLE posts RESTART IDENTITY CASCADE;
TRUNCATE TABLE user_followers RESTART IDENTITY CASCADE;
TRUNCATE TABLE user_weekly_statistic RESTART IDENTITY CASCADE;
TRUNCATE TABLE likes RESTART IDENTITY CASCADE;
TRUNCATE TABLE comments RESTART IDENTITY CASCADE;
TRUNCATE TABLE chats RESTART IDENTITY CASCADE;
TRUNCATE TABLE users RESTART IDENTITY CASCADE;
TRUNCATE TABLE chat_members RESTART IDENTITY CASCADE;
TRUNCATE TABLE messages RESTART IDENTITY CASCADE;


INSERT INTO users (id, username, email, password, role, first_name, last_name, address, is_active, last_login_time) VALUES
                                                                                                                        (100, 'admin', 'admin@onlybuns.com', '123', 'admin', 'John', 'Doe', 'Glavna 1, Beograd', true, '2025-09-08 15:30:00'),
                                                                                                                        (101, 'pera', 'pera.peric@onlybuns.com', '123', 'user', 'Pera', 'Perić', 'Sporedna 10, Novi Sad', true, '2025-08-08 15:00:00'),
                                                                                                                        (102, 'mika', 'janicevicivan90@gmail.com', '123', 'user', 'Mika', 'Mikić', 'Bulevar 5, Niš', true, '2025-08-07 18:00:00'),
                                                                                                                        (9999, 'SystemBot', 'system@onlybuns.com', '123', 'USER', 'System', 'Bot', 'SystemAddress', true, '2025-08-07 18:00:00'),
                                                                                                                        (104, 'ana', 'ana.anic@onlybuns.com', '123', 'user', 'Ana', 'Anić', 'Bulevar Oslobođenja 12, Novi Sad', true, '2025-09-10 10:00:00'),
                                                                                                                        (105, 'marko', 'marko@onlybuns.com', '123', 'user', 'Marko', 'Marković', 'Knez Mihailova 3, Beograd', true, '2025-09-10 11:00:00');

INSERT INTO posts (id, user_id, description, image_url, latitude, longitude, created_at) VALUES
                                                                                             (100, 101, 'Pogledajte mog zeku Duška! Uslikan u Novom Sadu.', 'http://localhost:8080/images/originals/pexels-caesisus-644787406-33323546.jpg', 45.267136, 19.833549, '2025-09-08 10:00:00'),
                                                                                             (101, 102, 'Moj ljubimac Gricko, najslađi zec u Nišu.', 'http://localhost:8080/images/originals/pexels-cerenvisuals-33285252.jpg', 43.320904, 21.895410, '2025-09-08 11:30:00'),
                                                                                             (102, 100, 'Dobrodošli na OnlyBuns! Podelite slike vaših ljubimaca. Pozdrav iz Beograda!', 'http://localhost:8080/images/originals/pexels-shvetsa-4588065.jpg', 44.787197, 20.457273, '2025-09-07 09:00:00'),
                                                                                             (103, 101, 'Cvet za sve vas.', 'http://localhost:8080/images/originals/pexels-tima-miroshnichenko-6846043.jpg', 44.787197, 20.457273, '2025-09-09 14:00:00'),
                                                                                             (104, 104, 'Moj mali krzneni prijatelj!', 'http://localhost:8080/images/originals/pexels-shvetsa-4588071.jpg', 45.267136, 19.833549, '2025-09-10 12:00:00'),
                                                                                             (105, 105, 'Nova zeka iz grada.', 'http://localhost:8080/images/originals/pexels-pixabay-247373.jpg', 44.787197, 20.457273, '2025-09-10 13:00:00'),
                                                                                             (106, 102, 'Novi apstraktni zec.', 'http://localhost:8080/images/originals/pexels-shvetsa-4588455.jpg', 43.320904, 21.895410, '2025-09-10 14:00:00'),
                                                                                             (107, 101, 'Pogledajte mog zeku Duška! Uslikan u Novom Sadu.', 'http://localhost:8080/images/originals/pexels-caesisus-644787406-33323546.jpg', 45.267136, 19.833549, '2025-09-08 10:00:00'),
                                                                                             (108, 102, 'Moj ljubimac Gricko, najslađi zec u Nišu.', 'http://localhost:8080/images/originals/pexels-cerenvisuals-33285252.jpg', 43.320904, 21.895410, '2025-09-08 11:30:00'),
                                                                                             (109, 100, 'Dobrodošli na OnlyBuns! Podelite slike vaših ljubimaca. Pozdrav iz Beograda!', 'http://localhost:8080/images/originals/pexels-shvetsa-4588065.jpg', 44.787197, 20.457273, '2025-09-07 09:00:00'),
                                                                                             (110, 101, 'Cvet za sve vas.', 'http://localhost:8080/images/originals/pexels-tima-miroshnichenko-6846043.jpg', 44.787197, 20.457273, '2025-09-09 14:00:00'),
                                                                                             (111, 104, 'Moj mali krzneni prijatelj!', 'http://localhost:8080/images/originals/pexels-shvetsa-4588071.jpg', 45.267136, 19.833549, '2025-09-10 12:00:00'),
                                                                                             (112, 105, 'Nova zeka iz grada.', 'http://localhost:8080/images/originals/pexels-pixabay-247373.jpg', 44.787197, 20.457273, '2025-09-10 13:00:00'),
                                                                                             (113, 102, 'Novi apstraktni zec.', 'http://localhost:8080/images/originals/pexels-shvetsa-4588455.jpg', 43.320904, 21.895410, '2025-09-10 14:00:00');

INSERT INTO likes (id, user_id, post_id, created_at) VALUES
                                                         (100, 100, 101, '2025-09-08 10:05:00'),
                                                         (101, 100, 102, '2025-09-08 10:06:00'),
                                                         (102, 100, 103, '2025-09-08 11:32:00'),
                                                         (103, 100, 104, '2025-09-09 14:05:00'),
                                                         (104, 100, 105, '2025-09-09 15:00:00'),
                                                         (105, 100, 106, '2025-09-09 16:00:00'),
                                                         (106, 100, 107, '2025-09-10 12:01:00'),
                                                         (107, 100, 108, '2025-09-10 13:01:00'),
                                                         (108, 100, 109, '2025-09-10 13:05:00'),
                                                         (109, 100, 110, '2025-09-08 10:05:00'),
                                                         (110, 101, 101, '2025-09-08 10:06:00'),
                                                         (111, 101, 102, '2025-09-08 11:32:00'),
                                                         (112, 101, 103, '2025-09-09 14:05:00'),
                                                         (113, 101, 104, '2025-09-09 15:00:00'),
                                                         (114, 101, 105, '2025-09-09 16:00:00'),
                                                         (115, 102, 101, '2025-09-10 12:01:00'),
                                                         (116, 102, 102, '2025-09-10 13:01:00'),
                                                         (117, 102, 103, '2025-09-10 13:05:00');

INSERT INTO comments (id, user_id, post_id, content, created_at) VALUES
                                                                     (100, 102, 100, 'Dobrodošao Pera! Super post, Duško je presladak!', '2025-09-08 10:15:00'),
                                                                     (101, 100, 100, 'Slažem se, odličan početak!', '2025-09-08 10:18:00'),
                                                                     (102, 101, 102, 'Hvala admine!', '2025-09-07 09:30:00'),
                                                                     (103, 105, 104, 'Presladak je! :)', '2025-09-10 12:05:00'),
                                                                     (104, 101, 105, 'Zec je jako kul!', '2025-09-10 13:10:00');

INSERT INTO chats (id, name, description, admin_id, created_at, is_active) VALUES
                                                                               (100, 'Opšta Diskusija', 'Kanal za sve članove da diskutuju o raznim temama.', 100, '2025-09-08 12:00:00', true),
                                                                               (101, 'Development Kanal', 'Razgovori o programiranju, Spring Boot-u i bazama.', 100, '2025-09-08 12:05:00', true),
                                                                               (102, 'Ljubitelji zečeva - Novi Sad', 'Grupa za sve vlasnike zečeva iz Novog Sada i okoline.', 101, '2025-09-09 10:00:00', true),
                                                                               (103, 'Arhivirani Kanal', 'Ovaj kanal više nije aktivan.', 100, '2025-01-01 12:00:00', false),
                                                                               (104, 'Sumnjiva Rabota Zec', 'Kupoprodaja zečeva... sa karakterom.', 101, '2025-09-10 09:00:00', true),
                                                                               (105, 'Beogradski zečevi', 'Ljubitelji zečeva iz Beograda.', 105, '2025-09-10 14:00:00', true);

INSERT INTO chat_members (id, user_id, chat_id, joined_at, role, is_active) VALUES
                                                                                (100, 100, 100, '2025-09-08 12:00:00', 'ADMIN', true),
                                                                                (101, 101, 100, '2025-09-08 12:01:00', 'MEMBER', true),
                                                                                (102, 102, 100, '2025-09-08 12:02:00', 'MEMBER', true),
                                                                                (103, 100, 101, '2025-09-08 12:05:00', 'ADMIN', true),
                                                                                (104, 101, 101, '2025-09-08 12:06:00', 'MEMBER', true),
                                                                                (105, 101, 102, '2025-09-09 10:00:00', 'ADMIN', true),
                                                                                (106, 102, 102, '2025-09-09 10:05:00', 'MEMBER', true),
                                                                                (107, 101, 104, '2025-09-10 09:00:00', 'ADMIN', true),
                                                                                (108, 102, 104, '2025-09-10 09:01:00', 'MEMBER', true),
                                                                                (109, 104, 102, '2025-09-10 10:05:00', 'MEMBER', true),
                                                                                (110, 105, 105, '2025-09-10 14:00:00', 'ADMIN', true);

INSERT INTO messages (id, content, "timestamp", type, chat_id, sender_id) VALUES
                                                                              (100, 'Zdravo svima!', '2025-09-08 12:03:00', 'TEXT', 100, 101),
                                                                              (101, 'Ćao Pera, dobrodošao!', '2025-09-08 12:04:00', 'TEXT', 100, 102),
                                                                              (102, 'Da li je neko imao iskustva sa Kafka streamovima?', '2025-09-08 13:00:00', 'TEXT', 101, 100),
                                                                              (103, 'Jesam ja malo, šta te konkretno zanima?', '2025-09-08 13:05:00', 'TEXT', 101, 101),
                                                                              (104, 'Dobrodošli svi u grupu! Ja sam Pera, admin.', '2025-09-09 10:01:00', 'TEXT', 102, 101),
                                                                              (105, 'Pozdrav Pera, čujem da si ekspert. Imam na prodaju jednog zeca, A klasa, skoro nov, malo prešao.', '2025-09-10 09:05:00', 'TEXT', 104, 102),
                                                                              (106, 'O, pa zdravo Mika. A klasa, kažeš? Daj malo više detalja. Koje je boje, koliko je star?', '2025-09-10 09:06:00', 'TEXT', 104, 101),
                                                                              (107, 'Šampionsko poreklo! Boja... unikatna bež sa apstraktnim detaljima. Star je taman koliko treba. Ima taj ''vintage'' izgled.', '2025-09-10 09:07:00', 'TEXT', 104, 102),
                                                                              (108, 'Apstraktni detalji? Zvuči kao da je upao u blato. Jel uopšte živ?', '2025-09-10 09:08:00', 'TEXT', 104, 101),
                                                                              (109, 'Mane? Ma kakvi! To su karakteristike. Recimo, ima aerodinamične uši - jedno je malo kraće, za manji otpor vazduha.', '2025-09-10 09:09:00', 'TEXT', 104, 102),
                                                                              (110, 'I energetski je efikasan, preferira da se odmara... horizontalno.', '2025-09-10 09:09:30', 'TEXT', 104, 102),
                                                                              (111, 'Čekaj, znači fali mu jedno uvo i stalno leži? Jel uopšte živ?', '2025-09-10 09:11:00', 'TEXT', 104, 101),
                                                                              (112, 'Živ?! Pa naravno! Pun je života! Skače u krug, doduše samo ulevo, ali sa neverovatnim entuzijazmom. Zovemo ga Ciklon.', '2025-09-10 09:12:00', 'TEXT', 104, 102),
                                                                              (113, 'Znači, ćopav, polugluv zec koji se vrti u krug. Nudim ti 200 dinara i kesicu smokija.', '2025-09-10 09:14:00', 'TEXT', 104, 101),
                                                                              (114, 'VREĐAŠ ME! ...ali pristajem ako su smoki sa kikirikijem.', '2025-09-10 09:15:00', 'TEXT', 104, 102),
                                                                              (115, 'Upozorenje: Transakcije koje uključuju ''aerodinamične'' zečeve se obavljaju na sopstvenu odgovornost.', '2025-09-10 09:20:00', 'SYSTEM', 104, 100);

INSERT INTO user_followers (follower_id, followee_id) VALUES
                                                          (101, 100),
                                                          (101, 102),
                                                          (102, 101),
                                                          (100, 102),
                                                          (104, 101),
                                                          (105, 100);

INSERT INTO user_weekly_statistic (user_id, likes, posts, followers) VALUES
                                                                         (100, 1, 1, 1),
                                                                         (101, 3, 2, 1),
                                                                         (102, 2, 1, 2),
                                                                         (104, 1, 1, 1),
                                                                         (105, 1, 1, 1);
