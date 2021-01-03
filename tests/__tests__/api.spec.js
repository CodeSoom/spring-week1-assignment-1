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
      it('responses empty array', async () => {
        await frisby.get('/tasks')
          .expect('status', 200)
          .expect('bodyContains', '[]');
      });
    });

    context('when tasks is exist', () => {
      beforeEach(async () => {
        await frisby.post('/tasks', { title });
      });

      it('responses tasks', async () => {
        await frisby.get('/tasks')
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

      it('responses task', async () => {
        await frisby.get(`/tasks/${id}`)
          .expect('status', 200)
          .expect('bodyContains', title);
      });
    });

    context('with not existing task id', () => {
      beforeEach(() => {
        id = 0;
      });

      it('responses 404 Not Found error', async () => {
        await frisby.get(`/tasks/${id}`)
          .expect('status', 404);
      });
    });
  });

  describe('POST /tasks', () => {
    it('responses 201 Created', async () => {
      await frisby.post('/tasks', { title })
        .expect('status', 201);
    });
  });

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
          .expect('status', 204);

        await frisby.get(`/tasks/${id}`)
          .expect('status', 404);
      });
    });

    context('with not existing task id', () => {
      beforeEach(async () => {
        id = 0;
      });

      it('responses 404 Not Found error', async () => {
        await frisby.delete(`/tasks/${id}`)
          .expect('status', 404);
      });
    });
  });
});
