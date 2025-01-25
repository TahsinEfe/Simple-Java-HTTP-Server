document.addEventListener('DOMContentLoaded', function() {
    const timeButton = document.getElementById('timeButton');
    const timeDisplay = document.getElementById('timeDisplay');

    const weatherButton = document.getElementById('weatherButton');
    const weatherDisplay = document.getElementById('weatherDisplay');

    const todoButton = document.getElementById('todoButton');
    const todoPanel = document.getElementById('todoPanel');
    const addButton = document.getElementById('addButton');
    const listButton = document.getElementById('listButton');
    const updateButton = document.getElementById('updateButton');
    const deleteButton = document.getElementById('deleteButton');
    const taskInput = document.getElementById('taskInput');
    const updateTaskInput = document.getElementById('updateTaskInput')
    const todoListDiv = document.getElementById('todoList');


    timeButton.addEventListener('click', function() {
        fetch('/api/time')
            .then(response => response.json())
            .then(data => timeDisplay.textContent = 'Time: ' + data.time)
            .catch(error => timeDisplay.textContent = 'Error: Could not fetch time');
    });

    weatherButton.addEventListener('click', function() {
        fetch('/api/weather')
            .then(response => response.json())
            .then(data => weatherDisplay.textContent = 'City: ' + data.city + ', Temperature: ' + data.temperature + ', Condition: ' + data.condition)
            .catch(error => weatherDisplay.textContent = 'Error: Could not fetch weather data');
    });

    todoButton.addEventListener('click', function() {
        todoPanel.style.display = todoPanel.style.display === 'none' ? 'block' : 'none';
    });

    addButton.addEventListener('click', function() {
        const task = taskInput.value.trim();
        if (task) {
            fetch(`/api/todos?task=${encodeURIComponent(task)}`, {
                method: 'POST'
            })
                .then(response => {
                    if (response.ok) {
                        taskInput.value = '';
                        displayTodos();
                    } else {
                        alert('Failed to add task!');
                    }
                })
                .catch(error => console.error('Error adding task', error));
        } else {
            alert('Task cannot be empty!');
        }
    });

    listButton.addEventListener('click', displayTodos);

    updateButton.addEventListener('click', function(){
      const task = taskInput.value.trim();
      const updatedTask = updateTaskInput.value.trim();
      if(task && updatedTask){
        fetch(`/api/todos?task=${encodeURIComponent(task)}&updatedTask=${encodeURIComponent(updatedTask)}`, {
          method: 'PUT'
        })
        .then(response =>{
          if (response.ok){
              taskInput.value = '';
              updateTaskInput.value = '';
            displayTodos();
          } else{
              alert('Failed to update task!');
          }
        })
        .catch(error => console.error('Error updating task', error));
      } else{
        alert('Task and updated task cannot be empty!');
      }
    })

    deleteButton.addEventListener('click', function() {
        const task = taskInput.value.trim();
        if (task) {
            fetch(`/api/todos?task=${encodeURIComponent(task)}`, {
                method: 'DELETE'
            })
                .then(response => {
                    if (response.ok) {
                        taskInput.value = '';
                        displayTodos();
                    } else {
                        alert('Failed to delete task!');
                    }
                })
                .catch(error => console.error('Error deleting task', error));
        } else {
            alert('Task cannot be empty');
        }
    });

   function displayTodos() {
        fetch('/api/todos')
            .then(response => response.json())
            .then(todos => {
                todoListDiv.innerHTML = '';
                if (todos.length === 0) {
                  todoListDiv.innerHTML = `<strong>No todos found</strong>`;
                } else {
                    let todoList = '<ul>';
                    todos.forEach(todo => {
                        todoList += `<li>${todo}</li>`;
                    });
                    todoList += '</ul>';
                    todoListDiv.innerHTML = todoList;
                }
            })
            .catch(error => console.error('Error fetching todos: ', error));
    }

});