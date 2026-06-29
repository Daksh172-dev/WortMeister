create table users (
  id bigint primary key auto_increment,
  public_id varchar(36) not null unique,
  created_at timestamp not null,
  updated_at timestamp not null,
  version bigint not null,
  email varchar(320) not null unique,
  password_hash varchar(255) not null,
  email_verified bit not null,
  enabled bit not null,
  role varchar(32) not null
);

create table user_profiles (
  id bigint primary key auto_increment,
  public_id varchar(36) not null unique,
  created_at timestamp not null,
  updated_at timestamp not null,
  version bigint not null,
  user_id bigint not null unique,
  display_name varchar(120) not null,
  avatar_url varchar(255),
  xp int not null,
  coins int not null,
  streak_days int not null,
  cefr_level varchar(16) not null,
  daily_reminder_enabled bit not null,
  sound_effects_enabled bit not null,
  constraint fk_profile_user foreign key (user_id) references users(id)
);

create table refresh_tokens (
  id bigint primary key auto_increment,
  public_id varchar(36) not null unique,
  created_at timestamp not null,
  updated_at timestamp not null,
  version bigint not null,
  user_id bigint not null,
  token_hash varchar(255) not null unique,
  expires_at timestamp not null,
  revoked bit not null,
  constraint fk_refresh_user foreign key (user_id) references users(id)
);

create table one_time_tokens (
  id bigint primary key auto_increment,
  public_id varchar(36) not null unique,
  created_at timestamp not null,
  updated_at timestamp not null,
  version bigint not null,
  user_id bigint not null,
  token_hash varchar(255) not null unique,
  purpose varchar(32) not null,
  expires_at timestamp not null,
  consumed bit not null,
  constraint fk_ott_user foreign key (user_id) references users(id)
);

create table lessons (
  id bigint primary key auto_increment,
  public_id varchar(36) not null unique,
  created_at timestamp not null,
  updated_at timestamp not null,
  version bigint not null,
  title varchar(255) not null,
  cefr_level varchar(16) not null,
  category varchar(64) not null,
  content varchar(4000) not null
);

create table vocabulary_items (
  id bigint primary key auto_increment,
  public_id varchar(36) not null unique,
  created_at timestamp not null,
  updated_at timestamp not null,
  version bigint not null,
  german varchar(255) not null,
  english varchar(255) not null,
  article varchar(255) not null,
  example_sentence varchar(255) not null,
  cefr_level varchar(16) not null
);

create table grammar_topics (
  id bigint primary key auto_increment,
  public_id varchar(36) not null unique,
  created_at timestamp not null,
  updated_at timestamp not null,
  version bigint not null,
  title varchar(255) not null,
  cefr_level varchar(16) not null,
  explanation varchar(4000) not null
);

create table media_assets (
  id bigint primary key auto_increment,
  public_id varchar(36) not null unique,
  created_at timestamp not null,
  updated_at timestamp not null,
  version bigint not null,
  url varchar(255) not null,
  type varchar(32) not null,
  description varchar(255) not null
);

create table learning_progress (
  id bigint primary key auto_increment,
  public_id varchar(36) not null unique,
  created_at timestamp not null,
  updated_at timestamp not null,
  version bigint not null,
  user_id bigint not null,
  lesson_id bigint not null,
  score int not null,
  completed bit not null,
  constraint fk_progress_user foreign key (user_id) references users(id),
  constraint fk_progress_lesson foreign key (lesson_id) references lessons(id)
);

create table pronunciation_attempts (
  id bigint primary key auto_increment,
  public_id varchar(36) not null unique,
  created_at timestamp not null,
  updated_at timestamp not null,
  version bigint not null,
  user_id bigint not null,
  phrase varchar(255) not null,
  audio_url varchar(255) not null,
  score int not null,
  feedback varchar(1000) not null,
  constraint fk_pron_user foreign key (user_id) references users(id)
);

create table achievements (
  id bigint primary key auto_increment,
  public_id varchar(36) not null unique,
  created_at timestamp not null,
  updated_at timestamp not null,
  version bigint not null,
  name varchar(255) not null,
  description varchar(255) not null,
  reward_coins int not null
);

create table ai_request_logs (
  id bigint primary key auto_increment,
  public_id varchar(36) not null unique,
  created_at timestamp not null,
  updated_at timestamp not null,
  version bigint not null,
  user_id bigint not null,
  feature varchar(64) not null,
  prompt varchar(4000) not null,
  response varchar(4000) not null,
  constraint fk_ai_user foreign key (user_id) references users(id)
);

create index idx_vocab_cefr on vocabulary_items(cefr_level);
create index idx_grammar_cefr on grammar_topics(cefr_level);
create index idx_media_type on media_assets(type);
create index idx_progress_user on learning_progress(user_id);
create index idx_pron_user on pronunciation_attempts(user_id);
