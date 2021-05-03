import frisby from 'frisby';

frisby.baseUrl('http://localhost:8000');

describe('tasks', () => {
  const title = '책 읽기';

  beforeEach(async () => {
    const res = await frisby.get('/tasks');
    const tasks = JSON.parse(res.body);
    await Promise.all(
      tasks.map(({ id }) => frisby.del(`/tasks/${id}`)),
    );
  });

  describe('GET /tasks', () => {
    context('when tasks is empty', () => {
<<<<<<< HEAD
      it('responses empty array', () => {
        frisby.get('/tasks')
=======
      it('responses empty array', async () => {
        await frisby.get('/tasks')
>>>>>>> First commit
          .expect('status', 200)
          .expect('bodyContains', '[]');
      });
    });

    context('when tasks is exist', () => {
      beforeEach(async () => {
        await frisby.post('/tasks', { title });
      });

<<<<<<< HEAD
      it('responses tasks', () => {
        frisby.get('/tasks')
=======
      it('responses tasks', async () => {
        await frisby.get('/tasks')
>>>>>>> First commit
          .expect('status', 200)
          .expect('bodyContains', `${title}`);
      });
    });
  });

  describe('GET /tasks/:id', () => {
    let id;

    context('with existing task id', () => {
      beforeEach(async () => {
        const res = await frisby.post('/tasks', { title });
        const task = JSON.parse(res.body);
        id = task.id;
      });

<<<<<<< HEAD
      it('responses task', () => {
        frisby.get(`/tasks/${id}`)
=======
      it('responses task', async () => {
        await frisby.get(`/tasks/${id}`)
>>>>>>> First commit
          .expect('status', 200)
          .expect('bodyContains', title);
      });
    });

    context('with not existing task id', () => {
      beforeEach(() => {
        id = 0;
      });

<<<<<<< HEAD
      it('responses 404 Not Found error', () => {
        frisby.get(`/tasks/${id}`)
=======
      it('responses 404 Not Found error', async () => {
        await frisby.get(`/tasks/${id}`)
>>>>>>> First commit
          .expect('status', 404);
      });
    });
  });

  describe('POST /tasks', () => {
<<<<<<< HEAD
    it('responses 201 Created', () => {
      frisby.post('/tasks', { title })
=======
    it('responses 201 Created', async () => {
      await frisby.post('/tasks', { title })
>>>>>>> First commit
        .expect('status', 201);
    });
  });

<<<<<<< HEAD
  describe('PUT or PATCH /tasks/:id', () => {
=======
  describe('PUT /tasks/:id', () => {
    let id;

    context('with existing task id', () => {
      beforeEach(async () => {
        const res = await frisby.post('/tasks', { title });
        const task = JSON.parse(res.body);
        id = task.id;
      });

      it('responses updated task', async () => {
        const res = await frisby.put(`/tasks/${id}`, { title: '밥 먹기' })
          .expect('status', 200);
        const task = JSON.parse(res.body);

        expect(task.title).toBe('밥 먹기');
      });
    });

    context('with not existing task id', () => {
      beforeEach(async () => {
        id = 0;
      });

      it('responses 404 Not Found error', async () => {
        await frisby.put(`/tasks/${id}`, { title: '밥 먹기' })
          .expect('status', 404);
      });
    });
  });

  describe('PATCH /tasks/:id', () => {
>>>>>>> First commit
    let id;

    context('with existing task id', () => {
      beforeEach(async () => {
        const res = await frisby.post('/tasks', { title });
        const task = JSON.parse(res.body);
        id = task.id;
      });

      it('responses updated task', async () => {
        const res = await frisby.put(`/tasks/${id}`, { title: '밥 먹기' })
          .expect('status', 200);
        const task = JSON.parse(res.body);

        expect(task.title).toBe('밥 먹기');
      });
    });

    context('with not existing task id', () => {
      beforeEach(async () => {
        id = 0;
      });

<<<<<<< HEAD
      it('responses 404 Not Found error', () => {
        frisby.put(`/tasks/${id}`, { title: '밥 먹기' })
=======
      it('responses 404 Not Found error', async () => {
        await frisby.put(`/tasks/${id}`, { title: '밥 먹기' })
>>>>>>> First commit
          .expect('status', 404);
      });
    });
  });

  describe('DELETE /tasks/:id', () => {
    let id;

    context('with existing task id', () => {
      beforeEach(async () => {
        const res = await frisby.post('/tasks', { title });
        const task = JSON.parse(res.body);
        id = task.id;
      });

      it('deletes task', async () => {
        await frisby.delete(`/tasks/${id}`)
<<<<<<< HEAD
          .expect('status', 200);
=======
          .expect('status', 204);
>>>>>>> First commit

        await frisby.get(`/tasks/${id}`)
          .expect('status', 404);
      });
    });

    context('with not existing task id', () => {
      beforeEach(async () => {
        id = 0;
      });

<<<<<<< HEAD
      it('responses 404 Not Found error', () => {
        frisby.delete(`/tasks/${id}`)
=======
      it('responses 404 Not Found error', async () => {
        await frisby.delete(`/tasks/${id}`)
>>>>>>> First commit
          .expect('status', 404);
      });
    });
  });
});
