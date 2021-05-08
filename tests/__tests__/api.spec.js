import frisby from 'frisby';

const title1 = '책 읽기';
const title2 = '밥 먹기';

frisby.baseUrl('http://localhost:8000');

describe('tasks', () => {
  beforeEach(async () => {
    const res = await frisby.get('/tasks');
    const tasks = JSON.parse(res.body);
    await Promise.all(
      Object.keys(tasks).map((id) => frisby.del(`/tasks/${id}`)),
    );
  });

  describe('GET /tasks', () => {
    context('when tasks is empty', () => {
      it('responses empty array', async () => {
        await frisby.get('/tasks')
          .expect('status', 200)
          .expect('bodyContains', '{}');
      });
    });

    context('when tasks is exist', () => {
      beforeEach(async () => {
        await frisby.post('/tasks', { title: title1 });
      });

      it('responses tasks', async () => {
        await frisby.get('/tasks')
          .expect('status', 200)
          .expect('bodyContains', `${title1}`);
      });
    });
  });

  describe('GET /tasks/:id', () => {
    let id;

    context('with existing task id', () => {
      beforeEach(async () => {
        const res = await frisby.post('/tasks', { title: title1 });
        const task = JSON.parse(res.body);
        id = task.id;
      });

      it('responses task', async () => {
        await frisby.get(`/tasks/${id}`)
          .expect('status', 200)
          .expect('bodyContains', title1);
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
      await frisby.post('/tasks', { title: title1 })
        .expect('status', 201);
    });
  });

  describe('PUT /tasks/:id', () => {
    let id;

    context('with existing task id', () => {
      beforeEach(async () => {
        const res = await frisby.post('/tasks', { title: title1 });
        const task = JSON.parse(res.body);
        id = task.id;
      });

      it('responses updated task', async () => {
        const res = await frisby.put(`/tasks/${id}`, { title: title2 })
          .expect('status', 200);
        const task = JSON.parse(res.body);

        expect(task.title).toBe(title2);
      });
    });

    context('with not existing task id', () => {
      beforeEach(async () => {
        id = 0;
      });

      it('responses 404 Not Found error', async () => {
        await frisby.put(`/tasks/${id}`, { title: title2 })
          .expect('status', 404);
      });
    });
  });

  describe('PATCH /tasks/:id', () => {
    let id;

    context('with existing task id', () => {
      beforeEach(async () => {
        const res = await frisby.post('/tasks', { title: title1 });
        const task = JSON.parse(res.body);
        id = task.id;
      });

      it('responses updated task', async () => {
        const res = await frisby.put(`/tasks/${id}`, { title: title2 })
          .expect('status', 200);
        const task = JSON.parse(res.body);

        expect(task.title).toBe(title2);
      });
    });

    context('with not existing task id', () => {
      beforeEach(async () => {
        id = 0;
      });

      it('responses 404 Not Found error', async () => {
        await frisby.put(`/tasks/${id}`, { title: title2 })
          .expect('status', 404);
      });
    });
  });

  describe('DELETE /tasks/:id', () => {
    let id;

    context('with existing task id', () => {
      beforeEach(async () => {
        const res = await frisby.post('/tasks', { title: title1 });
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
