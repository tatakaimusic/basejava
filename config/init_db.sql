create table public.resume
(
    uuid      character(36) primary key not null,
    full_name text                      not null
);

create table public.contact
(
    id          integer primary key not null default nextval('contact_id_seq'::regclass),
    type        text                not null,
    value       text                not null,
    resume_uuid character(36)       not null,
    foreign key (resume_uuid) references public.resume (uuid)
        match simple on update restrict on delete cascade
);

create unique index contact_uuid_type_index
    on contact (resume_uuid, type);

create table public.section
(
    id           integer primary key not null default nextval('text_section_id_seq'::regclass),
    resume_uuid  character(36)       not null,
    section_type character(36)       not null,
    content      text                not null,
    foreign key (resume_uuid) references public.resume (uuid)
        match simple on update restrict on delete cascade
);
create unique index text_section_id_key on section using btree (id);
create unique index text_section_resume_uuid_key on section using btree (resume_uuid);
create unique index text_section_type_key on section using btree (section_type);







