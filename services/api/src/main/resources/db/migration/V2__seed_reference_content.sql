insert into lessons (public_id, created_at, updated_at, version, title, cefr_level, category, content) values
('11111111-1111-1111-1111-111111111111', current_timestamp, current_timestamp, 0, 'Begrussungen im Alltag', 'A1', 'conversation', 'Learn greetings, introductions, and polite everyday phrases.'),
('22222222-2222-2222-2222-222222222222', current_timestamp, current_timestamp, 0, 'Artikel: der, die, das', 'A1', 'grammar', 'Understand German grammatical gender and common article patterns.'),
('33333333-3333-3333-3333-333333333333', current_timestamp, current_timestamp, 0, 'Im Cafe bestellen', 'A2', 'listening', 'Practice ordering drinks and snacks in a German cafe.');

insert into vocabulary_items (public_id, created_at, updated_at, version, german, english, article, example_sentence, cefr_level) values
('44444444-4444-4444-4444-444444444444', current_timestamp, current_timestamp, 0, 'Tisch', 'table', 'der', 'Der Tisch ist frei.', 'A1'),
('55555555-5555-5555-5555-555555555555', current_timestamp, current_timestamp, 0, 'Lampe', 'lamp', 'die', 'Die Lampe ist hell.', 'A1'),
('66666666-6666-6666-6666-666666666666', current_timestamp, current_timestamp, 0, 'Buch', 'book', 'das', 'Das Buch ist interessant.', 'A1');

insert into grammar_topics (public_id, created_at, updated_at, version, title, cefr_level, explanation) values
('77777777-7777-7777-7777-777777777777', current_timestamp, current_timestamp, 0, 'Personalpronomen', 'A1', 'Ich, du, er, sie, es, wir, ihr, sie are used to identify the subject of a sentence.'),
('88888888-8888-8888-8888-888888888888', current_timestamp, current_timestamp, 0, 'Perfekt mit haben', 'A2', 'Most German verbs form the spoken past with haben plus the past participle.');

insert into media_assets (public_id, created_at, updated_at, version, url, type, description) values
('99999999-9999-9999-9999-999999999999', current_timestamp, current_timestamp, 0, 'https://assets.wortmeister.local/images/cafe-ordering.jpg', 'IMAGE', 'Cafe ordering vocabulary scene.'),
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', current_timestamp, current_timestamp, 0, 'https://assets.wortmeister.local/audio/greetings-a1.mp3', 'AUDIO', 'A1 greetings pronunciation sample.');

insert into achievements (public_id, created_at, updated_at, version, name, description, reward_coins) values
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', current_timestamp, current_timestamp, 0, 'First Lesson', 'Complete your first German lesson.', 25),
('cccccccc-cccc-cccc-cccc-cccccccccccc', current_timestamp, current_timestamp, 0, 'Pronunciation Starter', 'Complete your first pronunciation attempt.', 30);
