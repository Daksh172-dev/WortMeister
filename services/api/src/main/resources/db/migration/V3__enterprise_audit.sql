create table audit_logs (
  id bigint primary key auto_increment,
  public_id varchar(36) not null unique,
  created_at timestamp not null,
  updated_at timestamp not null,
  version bigint not null,
  actor varchar(120) not null,
  action varchar(120) not null,
  resource varchar(120) not null,
  metadata varchar(2000) not null
);

create index idx_audit_created_at on audit_logs(created_at);
