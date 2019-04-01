package me.shamanov.resumebd.storage;

import me.shamanov.resumebd.model.ContactType;
import me.shamanov.resumebd.model.Resume;
import me.shamanov.resumebd.model.SectionType;
import me.shamanov.resumebd.model.TextSection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * Author: Mike
 * Date: 31.03.2019
 */

public abstract class AbstractStorageTest {
    protected Storage storage;
    private List<Resume> resumes;
    private Resume resume0, resume1, resume2;

    @Before
    public void setUp() {

        resumes = new ArrayList<Resume>() {
            {
                add(Resume.of("Александр Ковров", "Москва", "http://yandex.ru"));
                add(Resume.of("Михаил Иванов", "Санкт-Петербург", "http://google.ru"));
                add(Resume.of("Анна Егорова", "Кострома", "http://mail.ru"));
            }
        };
        //Filling resumes with information
        //Resume at index 0
        resume0 = resumes.get(0);
        resume0.addContact(ContactType.EMAIL, "kovrov@yandex.ru");
        resume0.addContact(ContactType.MOBILE, "+7 910 999 22 33");
        resume0.addSection(SectionType.POSITION, new TextSection("Java разработчик"));
        resume0.addSection(SectionType.ACCOMPLISHMENT,
                new TextSection(
                        "Разработал интернет-магазин для Dummy Co. Ltd. с нуля.",
                        "Разработал web-framework на основе EJB3, который вошёл в топ 5 перспективных фреймворков на Java, но это не точно.",
                        "Участвовал в создании искусственного интеллекта для робота \"Алёша\""
                ));

        //Resume at index 1
        resume1 = resumes.get(1);
        resume1.addContact(ContactType.EMAIL, "ivanov@gmail.com");
        resume1.addContact(ContactType.MOBILE, "+7 960 777 11 22");
        resume1.addSection(SectionType.POSITION, new TextSection("Таможенный представитель"));
        resume1.addSection(SectionType.ACCOMPLISHMENT,
                new TextSection(
                        "Работал с различным ПО для формирования всего пакета необхоидмых документов для оформления ДТ и прочих непонятных аббревиатур.",
                        "Осуществлял удалённое дкларирование по всей России, даже на Аляске.",
                        "Написал нескольго небольших программных оболочек на основе Excel для облегчения процесса работы, благодаря этому начальство сократило 10 сотрудников."
                ));

        //Resume at index 2
        resume2 = resumes.get(2);
        resume2.addContact(ContactType.EMAIL, "egorova@mail.ru");
        resume2.addContact(ContactType.MOBILE, "+7 915 987 99 25");
        resume2.addSection(SectionType.POSITION, new TextSection("Химик-лаборант"));
        resume2.addSection(SectionType.ACCOMPLISHMENT,
                new TextSection(
                        "Разработала новый вкус пива с использованием натуральных зерён кофе под маркой Java",
                        "Участвовала в процессе обучения новых сотрудников на позицию Химик-отравитель",
                        "Участвовала в согласовании работы двух предприятий - по производству колбасных изделий и туалетной бумаги."
                ));

        storage.clear();

        for (Resume r : resumes) {
            storage.save(r);
        }
    }

    @Test
    public void testUpdate() {
        resume0.setFullName("Александр Ершов");
        storage.update(resume0);
        Assert.assertEquals(resume0, storage.load(resume0.getId()));
    }

    @Test
    public void testSave() {
        Resume resume3 = Resume.of("Дмитрий Позов", "Пермь", "http://rambler.ru");
        storage.save(resume3);
        Assert.assertEquals(resume3, storage.load(resume3.getId()));
    }

    @Test
    public void testLoad() {
        Assert.assertEquals(resume1, storage.load(resume1.getId()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDelete() {
        storage.delete(resume2.getId());
        storage.load(resume2.getId());
    }


    @Test
    public void testSize() {
        int size = storage.size();
        Assert.assertEquals(3, size);
        Resume resume3 = Resume.of("Дмитрий Позов", "Пермь", "http://rambler.ru");
        storage.save(resume3);
        size = storage.size();
        Assert.assertEquals(4, size);
    }

    @Test
    public void testClear() {
        storage.clear();
        Assert.assertEquals(0, storage.size());
    }

    @Test
    public void getSortedResumeList() {
        Collections.sort(resumes);
        List<Resume> sortedFromStorage = storage.getSortedResumeList();
        Assert.assertEquals(resumes, sortedFromStorage);
    }
}