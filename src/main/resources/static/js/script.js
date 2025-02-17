$(document).ready(function () {
    let currentPage = 0;
    let size = 10;
    let totalPages = 0;
    const pathSegments = window.location.pathname.split('/');

    const $expandBtn = $(".toggle-btn");

    $expandBtn.on("click", function () {
        $("#sidebar").toggleClass("expand");
        $("#sidebar .toggle-btn img").toggleClass("rotate");
    });

    const $hamburgerDiv = $("#hamburgerButton");
    const $hamburgerBtn = $hamburgerDiv.find("button");
    const $sidebar = $("#sidebar");
    const $rightBoard = $("#rightBoard").children().not("#hamburgerButton");

    $hamburgerBtn.on("click", function (event) {
        event.stopPropagation();

        $sidebar.toggleClass("active");

        if ($sidebar.hasClass("active")) {
            $hamburgerDiv.addClass("non-active");
        } else {
            $hamburgerDiv.removeClass("non-active");
        }
    });

    $rightBoard.on("click", function () {
        if ($sidebar.hasClass("active")) {
            $sidebar.removeClass("active");
            $hamburgerDiv.removeClass("non-active");
        }
    });

    const title = $('title').text();
    $('#pageTitle').text(title);

    /* Back button */
    $('#cancelButton').on('click', function () {
        window.history.back();
    });

    /* ------------------------------ */

    /* User List Table */
    function fetchUsers(page = 0, size = 10) {
        const search = $('#searchUser').val();
        const role = $('#filterByRole').val();
        const url = `/api/users`;

        $.ajax({
            url: url,
            type: 'GET',
            data: {
                search: search,
                role: role,
                page: page,
                size: size
            },
            success: function (data) {
                renderUserList(data.content);
                updatePagination(data);
            },
            error: function (error) {
                console.error('Error fetching users:', error);
            }
        });
    }

    const userList = $('#userList');

    function renderUserList(users) {
        userList.empty();
        $.each(users, function (index, user) {
            const row = `
                    <tr>
                        <td>${user.username}</td>
                        <td style="background-color: #EBEFF3">${user.email}</td>
                        <td>${user.phoneNumber}</td>
                        <td style="background-color: #EBEFF3">${user.role}</td>
                        <td>${user.status}</td>
                        <td style="background-color: #EBEFF3">
                            <a onclick="viewUser(${user.userId})" class="btn">
                                <img src="/images/visible.png" alt="info icon">
                            </a>
                            <a onclick="editUser(${user.userId})" class="btn">
                                <img src="/images/edit-button.png" alt="edit icon">
                            </a>
                        </td>
                    </tr>
                `;
            userList.append(row);
        })
    }

    $("#searchUser").on("input", function () {
        fetchUsers(0);
    });

    $("#filterByRole").on("change", function () {
        fetchUsers(0);
    });

    if (userList.length > 0) {
        fetchUsers();
        handlePagination(fetchUsers);
    }
    /* END User List Table */

    /* ------------------------------ */

    /* User Detail */
    if (pathSegments.includes('users') && (pathSegments.includes('detail') || pathSegments.includes('edit'))) {
        const userId = pathSegments[pathSegments.length - 1];

        if (userId) {
            fetchUserDetail(userId);

            /* Active and Inactive User */
            $("#activeUserButton").on("click", function () {

                $.ajax({
                    type: 'POST',
                    url: '/api/users/updateStatus',
                    data: {
                        id: userId
                    },
                    success: function (response) {
                        if (response === 'Active') {
                            $('#activeUserButton').removeClass('btn-success').addClass('btn-danger');
                            $('#activeUserButton').text('Deactivate user');
                        } else {
                            $('#activeUserButton').removeClass('btn-danger').addClass('btn-success');
                            $('#activeUserButton').text('Activate user');
                        }
                        $("#userStatus").html(response);
                    },
                    error: function () {
                        alert("Error updating user status!");
                    }
                });
            });
            /* END Active and Inactive User */
        } else {
            window.location.href = '/error/error-404';
        }
    }

    function fetchUserDetail(userId) {
        $.ajax({
            url: `/api/users/${userId}`,
            type: 'GET',
            success: function (data) {
                renderUserDetail(data);
                renderUserEdit(data);
            },
            error: function (error) {
                console.error('Error fetching user detail:', error);
            }
        });
    }

    function renderUserDetail(user) {
        $("#userFullName").text(user.fullName);
        $("#userEmail").text(user.email);
        $("#userDOB").text(user.dateOfBirth);
        $("#userGender").text(user.gender);
        $("#userAddress").text(user.address);
        $("#userPhoneNumber").text(user.phoneNumber);
        $("#userDepartment").text(user.department);
        $("#userRole").text(user.role);
        $("#userStatus").text(user.status);
        $("#userNote").text(user.notes);

        if (user.status === 'Inactive') {
            $('#activeUserButton').removeClass('btn-danger').addClass('btn-success');
            $('#activeUserButton').text('Activate user');
        } else {
            $('#activeUserButton').removeClass('btn-success').addClass('btn-danger');
            $('#activeUserButton').text('Deactivate user');
        }
    }

    /* END User Detail */

    /* ------------------------------ */

    /* User Edit */
    function renderUserEdit(user) {
        $("input#fullName").val(user.fullName);
        $("input#email").val(user.email);
        $("input#dateOfBirth").val(user.dateOfBirth);
        $("input#address").val(user.address);
        $("input#phoneNumber").val(user.phoneNumber);
        $("select#gender").val(user.gender)
        $("select#role").val(user.role);
        $("select#status").val(user.status);
        $("select#department").val(user.department);
        $("textarea#notes").val(user.notes);
    }

    if (pathSegments.includes('users') && pathSegments.includes('edit')) {
        $("#updateUserFrom").on("submit", function (event) {
            event.preventDefault();
            const userId = pathSegments[pathSegments.length - 1];
            const formData = {
                userId: userId,
                fullName: $("#fullName").val(),
                email: $("#email").val(),
                dateOfBirth: $("#dateOfBirth").val(),
                address: $("#address").val(),
                phoneNumber: $("#phoneNumber").val(),
                gender: $("#gender").val(),
                role: $("#role").val(),
                department: $("#department").val(),
                status: $("#status").val(),
                notes: $("#notes").val()
            };

            $.ajax({
                url: `/api/users/${userId}`,
                type: 'PUT',
                contentType: 'application/json',
                data: JSON.stringify(formData),
                success: function (data) {
                    $("#resultMessage").text(data);
                    $("#resultMessage").addClass("text-success");
                    $(".invalid-input-user").text("");
                },
                error: function (error) {
                    if (error.status === 400) {
                        const errors = error.responseJSON;
                        $(".invalid-input-user").text("");

                        for (const key in errors) {
                            const errorMessage = errors[key];
                            $(`#${key}`).closest("div").find(".invalid-input-user").text(errorMessage);
                        }
                    } else {
                        $("#resultMessage").text("Update failed. Please try again.");
                        $("#resultMessage").addClass("text-danger");
                    }
                }
            });
        });
    }
    /* END User Edit */

    /* ------------------------------ */

    /* User Create */
    $("#createUserForm").on("submit", function (event) {
        event.preventDefault();
        const formData = {
            fullName: $("#fullName").val(),
            email: $("#email").val(),
            dateOfBirth: $("#dateOfBirth").val(),
            address: $("#address").val(),
            phoneNumber: $("#phoneNumber").val(),
            gender: $("#gender").val(),
            role: $("#role").val(),
            department: $("#department").val(),
            status: $("#status").val(),
            notes: $("#notes").val()
        };

        $.ajax({
            url: `/api/users`,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(formData),
            success: function (data) {
                $(".invalid-input-user").text("");
                localStorage.setItem('message', 'User created successfully');
                localStorage.setItem('status', 'success')
                window.location.href = '/users';
            },
            error: function (error) {
                if (error.status === 400) {
                    const errors = error.responseJSON;
                    $(".invalid-input-user").text("");

                    for (const key in errors) {
                        const errorMessage = errors[key];
                        $(`#${key}`).closest("div").find(".invalid-input-user").text(errorMessage);
                    }
                } else if (error.status === 409) {
                    $("#emailError").text(error.responseText);
                } else {
                    $("#resultMessage").text("Created failed. Please try again.");
                }
            }
        });
    });

    const message = localStorage.getItem('message');
    const status = localStorage.getItem('status');

    if (message) {
        $("#resultMessage").text(message);
        if (status === 'success') {
            $("#resultMessage").addClass("text-success");
        } else {
            $("#resultMessage").addClass("text-danger");
        }
        // Xóa thông báo khỏi localStorage sau khi hiển thị
        localStorage.removeItem('message');
        localStorage.removeItem('status');
    }
    /* END User Create */

    /* ------------------------------ */

    const recruiterOwner = $("#recruiterOwner");
    if (recruiterOwner.length > 0) {
        $.ajax({
            url: '/api/users/getRecruiters',
            type: 'GET',
            success: function (data) {
                renderRecruiterList(data);
            },
            error: function (error) {
                console.error('Error fetching recruiters:', error);
            }
        })

        function renderRecruiterList(data) {
            recruiterOwner.empty();
            recruiterOwner.append(
                '<option value="">Recruiter name</option>'
            );
            data.forEach(function (recruiter) {
                recruiterOwner.append(
                    `<option value="${recruiter.username}">${recruiter.fullName}</option>`
                );
            })
        }
    }

    /* Candidate List */
    function fetchCandidateList(page = 0, size = 10) {
        const search = $('#searchCandidate').val();
        const status = $('#filterByStatus').val();
        const url = `/api/candidates`;

        $.ajax({
            url: url,
            type: 'GET',
            data: {
                search: search,
                status: status,
                page: page,
                size: size
            },
            success: function (data) {
                renderCandidateList(data);
                updatePagination(data);
            },
            error: function (error) {
                console.log(error);
            }
        });
    }

    const candidateList = $("#candidateList");

    function renderCandidateList(candidates) {
        candidateList.empty();

        $.each(candidates.content, function (index, candidate) {
            const row = `
                <tr>
                    <td>${candidate.fullName}</td>
                    <td style="background-color: #EBEFF3">${candidate.email}</td>
                    <td>${candidate.phoneNumber}</td>
                    <td style="background-color: #EBEFF3">${candidate.currentPosition}</td>
                    <td>${candidate.recruiterOwner}</td>
                    <td style="background-color: #EBEFF3">${candidate.status}</td>
                    <td class="d-flex justify-content-center">
                        <a class="btn" href="/candidates/detail/${candidate.candidateId}">
                            <img src="/images/visible.png" alt="info icon">
                        </a>
                        <a href="/candidates/edit/${candidate.candidateId}" class="btn">
                            <img src="/images/edit-button.png" alt="edit icon">
                        </a>
                        <button type="submit" class="btn action-delete-btn" id="candidateDeleteBtn" data-bs-toggle="modal"
                                data-bs-target="#candidateDeleteConfirmationModal">
                            <svg class="w-6 h-6 text-gray-800 dark:text-white" aria-hidden="true"
                                 xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="none"
                                 viewBox="0 0 24 24">
                                <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round"
                                      stroke-width="2"
                                      d="M5 7h14m-9 3v8m4-8v8M10 3h4a1 1 0 0 1 1 1v3H9V4a1 1 0 0 1 1-1ZM6 7h12v13a1 1 0 0 1-1 1H7a1 1 0 0 1-1-1V7Z"/>
                            </svg>
                        </button>
                        <div class="modal fade" id="candidateDeleteConfirmationModal" tabindex="-1"
                             aria-labelledby="exampleModalLabel" aria-hidden="true">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-body d-flex flex-column align-items-center justify-content-center">
                                        <p>Are you sure you want to delete this candidate?</p>
                                    </div>
                                    <div class="modal-footer d-flex justify-content-center">
                                        <button type="button" class="btn w-50 m-0 border-end"
                                                data-bs-dismiss="modal">No
                                        </button>
                                        <button type="button" class="btn w-50 m-0 candidate-delete-ok-btn" data-candidate-id="${candidate.candidateId}">Yes</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </td>
                </tr>
            `;
            candidateList.append(row);
        });
    }

    if (candidateList.length > 0) {
        fetchCandidateList();
        handlePagination(fetchCandidateList);
    }

    $("#searchCandidateForm").on("submit", function (event) {
        event.preventDefault();
        fetchCandidateList();
    });
    /* END Candidate List */

    /* ------------------------------ */

    /* Candidate Detail */
    if (pathSegments.includes('candidates') && (pathSegments.includes('detail') || pathSegments.includes('edit'))) {
        const candidateId = pathSegments[pathSegments.length - 1];

        if (candidateId) {
            fetchCandidateDetail(candidateId);
        }
    }

    function fetchCandidateDetail(candidateId) {
        $.ajax({
            url: `/api/candidates/${candidateId}`,
            type: 'GET',
            success: function (data) {
                renderCandidateDetail(data);
                renderCandidateEdit(data)
            },
            error: function (error) {
                console.log(error);
            }
        });
    }

    function renderCandidateDetail(candidate) {
        $("#candidateFullName").text(candidate.fullName);
        $("#candidateEmail").text(candidate.email);
        $("#candidateDob").text(candidate.dateOfBirth);
        $("#candidateGender").text(candidate.gender);
        $("#candidateAddress").text(candidate.address);
        $("#candidatePhoneNumber").text(candidate.phoneNumber);
        $("#candidateCvFileName").text(candidate.cvFileName);
        $("#candidateSkills").text(candidate.skills);
        $("#candidateCurrentPosition").text(candidate.currentPosition);
        $("#candidateRecruiter").text(candidate.recruiterOwner);
        $("#candidateYearOfExperience").text(candidate.yearsOfExperience);
        $("#candidateHighestLevel").text(candidate.highestEducationLevel);
        $("#candidateStatus").text(candidate.status);
        $("#candidateNote").text(candidate.notes);

        if (candidate.status === 'Banned') {
            $('#banCandidateButton').removeClass('btn-danger').addClass('btn-success');
            $('#banCandidateButton').text('Open Candidate');
        } else {
            $('#banCandidateButton').removeClass('btn-success').addClass('btn-danger');
            $('#banCandidateButton').text('Ban Candidate');
        }

        $(document).on("click", "#candidateBanOkBtn", function (event) {
            event.preventDefault();
            $.ajax({
                url: `/api/candidates/banCandidate`,
                type: 'PUT',
                data: {
                    candidateId: candidate.candidateId
                },
                success: function (data) {
                    $('#candidateBanConfirmationModal').modal('hide');

                    if (data === 'Banned') {
                        $('#banCandidateButton').removeClass('btn-danger').addClass('btn-success');
                        $('#banCandidateButton').text('Open Candidate');
                    } else {
                        $('#banCandidateButton').removeClass('btn-success').addClass('btn-danger');
                        $('#banCandidateButton').text('Ban Candidate');
                    }
                },
                error: function (error) {
                    console.log(error);
                }
            });
        });

        $(document).on("click", "#downloadCvButton", function (event) {
            event.preventDefault();
            window.location.href = `/api/candidates/downloadCv?candidateId=${candidate.candidateId}`;
        });

        $("#candidateEditButton").on("click", function () {
            window.location.href = `/candidates/edit/${candidate.candidateId}`;
        });
    }

    /* END Candidate Detail */

    /* ------------------------------ */

    /* Candidate Edit */

    function renderCandidateEdit(candidate) {
        $("#fullName").val(candidate.fullName);
        $("#email").val(candidate.email);
        $("#dateOfBirth").val(candidate.dateOfBirth);
        $("#address").val(candidate.address);
        $("#phoneNumber").val(candidate.phoneNumber);
        $("#gender").val(candidate.gender)
        $("#cvFileNameText").text(candidate.cvFileName);
        $("#currentPosition").val(candidate.currentPosition.trim());
        $("#statusText").text(candidate.status);
        $("#status").val(candidate.status);
        $("#yearsOfExperience").val(candidate.yearsOfExperience);
        const highestEducationLevel = candidate.highestEducationLevel.replace(/’/g, "'");
        $("#highestEducationLevel").val(highestEducationLevel);
        $("#notes").val(candidate.notes);

        $("#recruiterOwner").val(candidate.recruiterOwner);

        var selectedSkills = candidate.skills.split(',').map(skill => skill.trim());

        if (selectedSkills && Array.isArray(selectedSkills)) {
            selectedSkills.forEach(skill => {
                multipleCancelButton.setChoiceByValue(skill);
            });
        }
    }

    if (pathSegments.includes('candidates') && pathSegments.includes('edit')) {
        const skillsSelectTag = $("#skillsSelectTag")[0];
        let multipleCancelButton;
        if (skillsSelectTag.length > 0) {
            multipleCancelButton = new Choices(skillsSelectTag, {
                removeItemButton: true,
                maxItemCount: 10,
                searchResultLimit: 10,
                renderChoiceLimit: 10
            });
        }

        $("#updateCandidateForm").on("submit", function (event) {
            event.preventDefault();
            const candidateId = pathSegments[pathSegments.length - 1];
            let selectedSkills = [];
            if (skillsSelectTag.length > 0) {
                selectedSkills = Array.from(skillsSelectTag.options).filter(option => option.selected).map(option => option.value);
            }
            const formData = {
                candidateId: candidateId,
                fullName: $("#fullName").val(),
                email: $("#email").val(),
                dateOfBirth: $("#dateOfBirth").val(),
                address: $("#address").val(),
                phoneNumber: $("#phoneNumber").val(),
                gender: $("#gender").val(),
                currentPosition: $("#currentPosition").val(),
                status: $("#status").val(),
                yearsOfExperience: $("#yearsOfExperience").val(),
                highestEducationLevel: $("#highestEducationLevel").val(),
                recruiterOwner: $("#recruiterOwner").val(),
                notes: $("#notes").val(),
                skills: selectedSkills.length > 0 ? selectedSkills.join(", ") : null
            };

            console.log(formData);

            $.ajax({
                url: `/api/candidates/${candidateId}`,
                type: 'PUT',
                contentType: 'application/json',
                data: JSON.stringify(formData),
                success: function (data) {
                    $("#resultMessage").text(data);
                    $("#resultMessage").addClass("text-success");
                    $(".invalid-input-user").text("");
                },
                error: function (error) {
                    if (error.status === 400) {
                        const errors = error.responseJSON;
                        $(".invalid-input-user").text("");

                        for (const key in errors) {
                            const errorMessage = errors[key];
                            $(`#${key}`).closest("div").find(".invalid-input-user").text(errorMessage);
                        }
                    } else {
                        $("#resultMessage").text("Created failed. Please try again.");
                        $("#resultMessage").addClass("text-danger");
                    }
                }
            });
        });
    }
    /* END Candidate Edit */

    /* ------------------------------ */

    /* Candidate Create */
    $("#createCandidateForm").on("submit", function (event) {
        event.preventDefault();
        let selectedSkills = [];
        if (skillsSelectTag.length > 0) {
            selectedSkills = Array.from(skillsSelectTag.options).filter(option => option.selected).map(option => option.value);
        }
        let candidateDTO = {
            fullName: $("#fullName").val(),
            email: $("#email").val(),
            dateOfBirth: $("#dateOfBirth").val(),
            address: $("#address").val(),
            phoneNumber: $("#phoneNumber").val(),
            gender: $("#gender").val(),
            currentPosition: $("#currentPosition").val(),
            status: $("#status").val(),
            yearsOfExperience: $("#yearsOfExperience").val(),
            highestEducationLevel: $("#highestEducationLevel").val(),
            recruiterOwner: $("#recruiterOwner").val(),
            notes: $("#notes").val(),
            skills: selectedSkills.length > 0 ? selectedSkills.join(", ") : null
        };

        let formData = new FormData();
        formData.append("candidateDTO", new Blob([JSON.stringify(candidateDTO)], { type: "application/json" }));


        const cvFile = $("#cvFileName")[0].files[0];
        if (cvFile) {
            formData.append("cv", cvFile);
        }

        $.ajax({
            url: '/api/candidates',
            type: 'POST',
            enctype: 'multipart/form-data',
            data: formData,
            processData: false,
            contentType: false,
            success: function (data) {
                $(".invalid-input-user").text("");
                localStorage.setItem('message', 'Candidate created successfully');
                localStorage.setItem('status', 'success')
                window.location.href = '/candidates';
            },
            error: function (error) {
                if (error.status === 400) {
                    const errors = error.responseJSON;
                    $(".invalid-input-user").text("");

                    for (const key in errors) {
                        const errorMessage = errors[key];
                        $(`#${key}`).closest("div").find(".invalid-input-user").text(errorMessage);
                    }
                } else if (error.status === 409) {
                    $("#emailError").text(error.responseText);
                } else {
                    $("#resultMessage").text("Created failed. Please try again.");
                }
            }
        });
    });
    /* END Candidate Create */

    /* ------------------------------ */

    /* Candidate Delete */
    $(document).on("click", ".candidate-delete-ok-btn", function (event) {
        event.preventDefault();
        const candidateId = $(this).data('candidate-id');
        $.ajax({
            url: `/api/candidates/${candidateId}`,
            type: 'DELETE',
            success: function (data) {
                localStorage.setItem('message', 'Candidate deleted successfully');
                localStorage.setItem('status', 'success');
                $('#candidateDeleteConfirmationModal').modal('hide');
                fetchCandidateList();
            },
            error: function (error) {
                console.log(error);
            }
        });
    });
    /* END Candidate Delete */

    /* ------------------------------ */

    /* Job List */
    function fetchJobList(page = 0, size = 10) {
        const search = $("#searchJob").val();
        const status = $("#filterByStatus").val();
        const url = `/api/jobs`;

        $.ajax({
            url: url,
            type: 'GET',
            data: {
                search: search,
                status: status,
                page: page,
                size: size
            },
            success: function (data) {
                renderJobList(data);
                updatePagination(data);
            },
            error: function (error) {
                console.log(error);
            }
        });
    }

    const jobList = $("#jobList");
    function renderJobList(data) {
        jobList.empty();

        $.each(data.content, function (index, job) {
            jobList.append(`
                <tr>
                    <td>${job.jobTitle}</td>
                    <td style="background-color: #EBEFF3">${job.requiredSkills}</td>
                    <td>${job.startDate}</td>
                    <td style="background-color: #EBEFF3">${job.endDate}</td>
                    <td>${job.level}</td>
                    <td style="background-color: #EBEFF3">${job.status}</td>
                    <td class="d-flex justify-content-center">
                        <a class="btn" href="/jobs/detail/${job.jobId}">
                            <img src="/images/visible.png" alt="info icon">
                        </a>
                        <a href="/jobs/edit/${job.jobId}" class="btn">
                            <img src="/images/edit-button.png" alt="edit icon">
                        </a>
                        <button type="submit" class="btn action-delete-btn" id="jobDeleteBtn" data-bs-toggle="modal"
                                data-bs-target="#jobDeleteConfirmationModal">
                            <svg class="w-6 h-6 text-gray-800 dark:text-white" aria-hidden="true"
                                 xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="none"
                                 viewBox="0 0 24 24">
                                <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round"
                                      stroke-width="2"
                                      d="M5 7h14m-9 3v8m4-8v8M10 3h4a1 1 0 0 1 1 1v3H9V4a1 1 0 0 1 1-1ZM6 7h12v13a1 1 0 0 1-1 1H7a1 1 0 0 1-1-1V7Z"/>
                            </svg>
                        </button>
                        <div class="modal fade" id="jobDeleteConfirmationModal" tabindex="-1"
                             aria-labelledby="exampleModalLabel" aria-hidden="true">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-body d-flex flex-column align-items-center justify-content-center">
                                        <p>Are you sure you want to delete this job?</p>
                                    </div>
                                    <div class="modal-footer d-flex justify-content-center">
                                        <button type="button" class="btn w-50 m-0 border-end"
                                                data-bs-dismiss="modal">No
                                        </button>
                                        <button type="submit" class="btn w-50 m-0 job-delete-ok-button" data-job-id="${job.jobId}">Yes
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </td>
                </tr>
            `)
        });
    }

    if (jobList.length > 0) {
        fetchJobList();
        handlePagination(fetchJobList);
    }

    $("#searchJobForm").on("submit", function (event) {
        event.preventDefault();
        fetchJobList();
    });
    /* END Job List */

    /* ------------------------------ */

    /* Job Detail */
    if (pathSegments.includes("jobs") && (pathSegments.includes("detail") || pathSegments.includes("edit"))) {
        const jobId = pathSegments[pathSegments.length - 1];

        if (jobId) {
            fetchJobDetail(jobId);
        }
    }

    function fetchJobDetail(jobId) {
        $.ajax({
            url: `/api/jobs/${jobId}`,
            type: 'GET',
            success: function (data) {
                renderJobDetail(data);
            },
            error: function (error) {
                console.log(error);
            }
        })
    }
    function renderJobDetail(job) {
        $("#jobCreateAt").text(job.createdAt);
        $("#jobUpdateAt").text(job.updatedAt);

        $("#jobTitle").text(job.jobTitle);
        $("#jobSkills").text(job.requiredSkills);
        $("#startDate").text(job.startDate);
        $("#endDate").text(job.endDate);
        $("#salaryRangeFrom").text(job.salaryRangeFrom);
        $("#salaryRangeTo").text(job.salaryRangeTo);
        $("#jobBenefits").text(job.benefits);
        $("#workingAddress").text(job.workingAddress);
        $("#jobLevel").text(job.level);
        $("#jobStatus").text(job.status);
        $("#jobDescription").text(job.description);

        if ($("input#jobTitle").length > 0) {
            $("input#jobTitle").val(job.jobTitle);
        }
        if ($("input#startDate").length > 0) {
            $("input#startDate").val(job.startDate);
        }
        if ($("input#endDate").length > 0) {
            $("input#endDate").val(job.endDate);
        }
        if ($("input#salaryRangeFrom").length > 0) {
            $("input#salaryRangeFrom").val(job.salaryRangeFrom);
        }
        if ($("input#salaryRangeTo").length > 0) {
            $("input#salaryRangeTo").val(job.salaryRangeTo);
        }
        if ($("input#workingAddress").length > 0) {
            $("input#workingAddress").val(job.workingAddress);
        }
        if ($("textarea#description").length > 0) {
            $("textarea#description").val(job.description);
        }

        const skillsSelectTag = $("#skillsSelectTag")[0];
        const benefitsSelectTag = $("#benefitsSelectTag")[0];
        const levelsSelectTag = $("#levelsSelectTag")[0];
        let multipleSkillsSelect;
        let multipleBenefitsSelect;
        let multipleLevelsSelect;
        if (skillsSelectTag.length > 0) {
            multipleSkillsSelect = new Choices(skillsSelectTag, {
                removeItemButton: true,
                maxItemCount: 10,
                searchResultLimit: 10,
                renderChoiceLimit: 10
            });
        }
        if (benefitsSelectTag.length > 0) {
            multipleBenefitsSelect = new Choices(benefitsSelectTag, {
                removeItemButton: true,
                maxItemCount: 5,
                searchResultLimit: 5,
                renderChoiceLimit: 5
            });
        }
        if (levelsSelectTag.length > 0) {
            multipleLevelsSelect = new Choices(levelsSelectTag, {
                removeItemButton: true,
                maxItemCount: 5,
                searchResultLimit: 5,
                renderChoiceLimit: 5
            });
        }

        var selectedSkills = job.requiredSkills.split(',').map(skill => skill.trim());
        if (selectedSkills && Array.isArray(selectedSkills)) {
            selectedSkills.forEach(skill => {
                multipleSkillsSelect.setChoiceByValue(skill);
            });
        }

        var selectedBenefits = job.benefits.split(',').map(benefit => benefit.trim());
        if (selectedBenefits && Array.isArray(selectedBenefits)) {
            selectedBenefits.forEach(benefit => {
                multipleBenefitsSelect.setChoiceByValue(benefit);
            });
        }

        var selectedLevels = job.level.split(',').map(level => level.trim());
        if (selectedLevels && Array.isArray(selectedLevels)) {
            selectedLevels.forEach(level => {
                multipleLevelsSelect.setChoiceByValue(level);
            });
        }

        $("#jobEditButton").on("click", function () {
            window.location.href = `/jobs/edit/${job.jobId}`;
        });
    }
    /* END Job Detail */

    /* ------------------------------ */

    /* Job Edit */

    if (pathSegments.includes("jobs") && pathSegments.includes("edit")) {
        $("#jobEditForm").on("submit", function (event) {
            event.preventDefault();
            const jobId = pathSegments[pathSegments.length - 1];
            const skillsSelectTag = $("#skillsSelectTag")[0];
            const benefitsSelectTag = $("#benefitsSelectTag")[0];
            const levelsSelectTag = $("#levelsSelectTag")[0];
            let selectedSkills = [];
            if (skillsSelectTag.length > 0) {
                selectedSkills = Array.from(skillsSelectTag.options).filter(option => option.selected).map(option => option.value);
            }
            let selectedBenefits = [];
            if (benefitsSelectTag.length > 0) {
                selectedBenefits = Array.from(benefitsSelectTag.options).filter(option => option.selected).map(option => option.value);
            }
            let selectedLevels = [];
            if (levelsSelectTag.length > 0) {
                selectedLevels = Array.from(levelsSelectTag.options).filter(option => option.selected).map(option => option.value);
            }
            const formData = {
                jobId: jobId,
                jobTitle: $("input#jobTitle").val(),
                requiredSkills: selectedSkills.length > 0 ? selectedSkills.join(', ') : null,
                startDate: $("input#startDate").val(),
                endDate: $("input#endDate").val(),
                salaryRangeFrom: $("input#salaryRangeFrom").val(),
                salaryRangeTo: $("input#salaryRangeTo").val(),
                workingAddress: $("input#workingAddress").val(),
                benefits: selectedBenefits.length > 0 ? selectedBenefits.join(', ') : null,
                level: selectedLevels.length > 0 ? selectedLevels.join(', ') : null,
                description: $("textarea#description").val(),
            }

            $.ajax({
                url: `/api/jobs/${jobId}`,
                type: 'PUT',
                contentType: 'application/json',
                data: JSON.stringify(formData),
                success: function (data) {
                    $("#resultMessage").text(data);
                    $("#resultMessage").addClass("text-success");
                    $(".invalid-input-user").text("");
                },
                error: function (error) {
                    if (error.status === 400) {
                        const errors = error.responseJSON;
                        $(".invalid-input-user").text("");

                        for (const key in errors) {
                            const errorMessage = errors[key];
                            $(`#${key}`).closest("div").find(".invalid-input-user").text(errorMessage);
                        }
                    } else {
                        $("#resultMessage").text("Created failed. Please try again.");
                        $("#resultMessage").addClass("text-danger");
                    }
                }
            });
        });
    }
    /* END Job Edit */



    /* Job Delete */
    $(document).on("click", ".job-delete-ok-button", function (event) {
        event.preventDefault();
        const jobId = $(this).data("job-id");
        $.ajax({
            url: `/api/jobs/${jobId}`,
            type: 'DELETE',
            success: function (data) {
                localStorage.setItem("message", "Job deleted successfully!");
                localStorage.setItem("status", "success");
                $("#jobDeleteConfirmationModal").modal("hide");
                fetchJobList();
            },
            error: function (error) {
                console.log(error);
            }
        });
    });
    /* END Job Delete */

    /* ------------------------------ */

    /* Job Create */
    if (pathSegments.includes("jobs") && pathSegments.includes("create")) {
        const skillsSelectTag = $("#skillsSelectTag")[0];
        const benefitsSelectTag = $("#benefitsSelectTag")[0];
        const levelsSelectTag = $("#levelsSelectTag")[0];
        let multipleSkillsSelect;
        let multipleBenefitsSelect;
        let multipleLevelsSelect;
        if (skillsSelectTag.length > 0) {
            multipleSkillsSelect = new Choices(skillsSelectTag, {
                removeItemButton: true,
                maxItemCount: 10,
                searchResultLimit: 10,
                renderChoiceLimit: 10
            });
        }
        if (benefitsSelectTag.length > 0) {
            multipleBenefitsSelect = new Choices(benefitsSelectTag, {
                removeItemButton: true,
                maxItemCount: 5,
                searchResultLimit: 5,
                renderChoiceLimit: 5
            });
        }
        if (levelsSelectTag.length > 0) {
            multipleLevelsSelect = new Choices(levelsSelectTag, {
                removeItemButton: true,
                maxItemCount: 5,
                searchResultLimit: 5,
                renderChoiceLimit: 5
            });
        }

        $("#createJobForm").on("submit", function(event) {
            event.preventDefault();
            let selectedSkills = [];
            if (skillsSelectTag.length > 0) {
                selectedSkills = Array.from(skillsSelectTag.options).filter(option => option.selected).map(option => option.value);
            }
            let selectedBenefits = [];
            if (benefitsSelectTag.length > 0) {
                selectedBenefits = Array.from(benefitsSelectTag.options).filter(option => option.selected).map(option => option.value);
            }
            let selectedLevels = [];
            if (levelsSelectTag.length > 0) {
                selectedLevels = Array.from(levelsSelectTag.options).filter(option => option.selected).map(option => option.value);
            }
            const formData = {
                jobTitle: $("input#jobTitle").val(),
                requiredSkills: selectedSkills.length > 0 ? selectedSkills.join(', ') : null,
                startDate: $("input#startDate").val(),
                endDate: $("input#endDate").val(),
                salaryRangeFrom: $("input#salaryRangeFrom").val(),
                salaryRangeTo: $("input#salaryRangeTo").val(),
                workingAddress: $("input#workingAddress").val(),
                benefits: selectedBenefits.length > 0 ? selectedBenefits.join(', ') : null,
                level: selectedLevels.length > 0 ? selectedLevels.join(', ') : null,
                description: $("textarea#description").val(),
            }

            $.ajax({
                url: `/api/jobs`,
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(formData),
                success: function (data) {
                    $(".invalid-input-user").text("");
                    localStorage.setItem('message', 'Job created successfully');
                    localStorage.setItem('status', 'success')
                    window.location.href = '/jobs';
                },
                error: function (error) {
                    if (error.status === 400) {
                        const errors = error.responseJSON;
                        $(".invalid-input-user").text("");

                        for (const key in errors) {
                            const errorMessage = errors[key];
                            $(`#${key}`).closest("div").find(".invalid-input-user").text(errorMessage);
                        }
                    } else {
                        $("#resultMessage").text("Created failed. Please try again.");
                    }
                }
            });
        });
    }
    /* END Job Create */

    /* ----------------------------------------------------------------------------------------------------------- */

    /* Schedule List */
    const interviewerSelect = $(".interviewer-select");
    function fetchInterviewerList() {
        $.ajax({
            url: '/api/users/getInterviewers',
            type: 'GET',
            success: function (data) {
                renderInterviewerList(data);
            },
            error: function (error) {
                console.error('Error fetching interviewers:', error);
            }
        })
    }
    function renderInterviewerList(data) {
        const interviewersSelectTag = $("#interviewersSelectTag");
        interviewersSelectTag.empty();
        data.forEach(function (interviewer) {
            interviewersSelectTag.append(
                `<option value="${interviewer.userId}">${interviewer.fullName}</option>`
            );
        });
    }
    if (pathSegments.includes("interview-schedules")) {
        if (interviewerSelect.length > 0) {
            fetchInterviewerList();
        }
    }
    function fetchScheduleList(page = 0, size = 10) {
        const search = $("#searchSchedule").val();
        const interviewerId = $("#filterByInterviewer").val();
        const status = $("#filterByStatus").val();
        const url = `/api/interview-schedules`;

        $.ajax({
            url: url,
            type: 'GET',
            data: {
                search: search,
                interviewerId: interviewerId,
                status: status,
                page: page,
                size: size
            },
            success: function (data) {
                renderScheduleList(data);
                updatePagination(data);
            },
            error: function (error) {
                console.log(error);
            }
        });
    }

    const scheduleList = $("#scheduleList");
    function renderScheduleList(data) {
        scheduleList.empty();

        $.each(data.content, function (index, interviewSchedule) {
            const scheduleFrom = formatTime(interviewSchedule.scheduleFrom);
            const scheduleTo = formatTime(interviewSchedule.scheduleTo);
            scheduleList.append(`
                <tr>
                    <td>${interviewSchedule.interviewTitle}</td>
                    <td style="background-color: #EBEFF3">${interviewSchedule.candidateName}</td>
                    <td>${interviewSchedule.interviewerNames}</td>
                    <td style="background-color: #EBEFF3">${interviewSchedule.scheduleDate} ${scheduleFrom} - ${scheduleTo}</td>
                    <td>${interviewSchedule.result}</td>
                    <td style="background-color: #EBEFF3">${interviewSchedule.status}</td>
                    <td >${interviewSchedule.jobTitle}</td>
                    <td class="d-flex justify-content-center" style="background-color: #EBEFF3">
                        <a class="btn" href="/interview-schedules/detail/${interviewSchedule.scheduleId}">
                            <img src="/images/visible.png" alt="info icon">
                        </a>
                        <a href="/interview-schedules/edit/${interviewSchedule.scheduleId}" class="btn">
                            <img src="/images/edit-button.png" alt="edit icon">
                        </a>
                        <a th:href="/interview-schedules/edit/${interviewSchedule.scheduleId}" class="btn">
                            <img src="/images/submit.png" alt="submit icon" width="20" height="20">
                        </a>
                    </td>
                </tr>
            `)
        });
    }

    if (scheduleList.length > 0) {
        fetchScheduleList();
        handlePagination(fetchScheduleList);
    }

    $("#searchScheduleForm").on("submit", function (event) {
        event.preventDefault();
        fetchScheduleList();
    });
    /* END Schedule List */

    /* ------------------------------ */

    /* Schedule Detail */
    if (pathSegments.includes("interview-schedules") && pathSegments.includes("detail")) {
        const scheduleId = pathSegments[pathSegments.length - 1];

        if (scheduleId) {
            fetchScheduleDetail(scheduleId);
        }
    }

    function fetchScheduleDetail(scheduleId) {
        $.ajax({
            url: `/api/interview-schedules/${scheduleId}`,
            type: 'GET',
            success: function (data) {
                renderScheduleDetail(data);
            },
            error: function (error) {
                console.log(error);
            }
        })
    }
    function renderScheduleDetail(schedule) {
        $("#createAt").text(schedule.createdAt);
        $("#updateAt").text(schedule.updatedAt);

        $("#scheduleTitle").text(schedule.scheduleTitle);
        $("#scheduleJobTitle").text(schedule.candidateName);
        $("#scheduleCandidateName").text(schedule.jobTitle);
        $("#scheduleInterviewers").text(schedule.interviewersUsernameList);
        $("#scheduleTime").text(schedule.scheduleDate);
        $("#scheduleLocation").text(schedule.location);
        $("#scheduleFrom").text(schedule.scheduleFrom);
        $("#scheduleTo").text(schedule.scheduleTo);
        $("#scheduleRecruiterOwner").text(schedule.recruiterOwner);
        $("#scheduleNotes").text(schedule.notes);
        $("#scheduleMeetingId").text(schedule.meetingId);
        $("#scheduleResult").text(schedule.result);
        $("#scheduleStatus").text(schedule.status);

        $("#scheduleEditButton").on("click", function () {
            window.location.href = `/interview-schedules/edit/${schedule.scheduleId}`;
        });
    }
    /* END Schedule Detail */

    /* ------------------------------ */

    /* Schedule Edit */
    const selectCandidate = $("#candidateId");
    function fetchOpenCandidateListForEdit(candidateId, callback) {
        $.ajax({
            url: '/api/candidates/getCandidatesForScheduleEdit',
            type: 'GET',
            data: {
                candidateId: candidateId
            },
            success: function (data) {
                renderOpenCandidateList(data);

                if (callback) {
                    callback();
                }
            },
            error: function (error) {
                console.error('Error fetching candidates:', error);
            }
        });
    }
    function fetchOpenCandidateList() {
        $.ajax({
            url: '/api/candidates/getOpenCandidates',
            type: 'GET',
            success: function (data) {
                renderOpenCandidateList(data);
            },
            error: function (error) {
                console.error('Error fetching candidates:', error);
            }
        });
    }
    function renderOpenCandidateList(data) {
        selectCandidate.empty();
        selectCandidate.append(
            '<option value="">Select Candidate</option>'
        );
        data.forEach(function (candidate) {
            selectCandidate.append(
                `<option value="${candidate.candidateId}">${candidate.fullName}</option>`
            );
        })
    }

    const selectJob = $("#jobSelect");
    function fetchOpenJobList() {
        $.ajax({
            url: '/api/jobs/getOpenJobs',
            type: 'GET',
            success: function (data) {
                renderOpenJobList(data);
            },
            error: function (error) {
                console.error('Error fetching jobs:', error);
            }
        })
    }
    function renderOpenJobList(data) {
        selectJob.empty();
        selectJob.append(
            '<option value="">Select a job</option>'
        );
        data.forEach(function (job) {
            selectJob.append(
                `<option value="${job.jobId}">${job.jobTitle}</option>`
            );
        })
    }

    if (pathSegments.includes("interview-schedules") && pathSegments.includes("edit")) {
        const scheduleId = pathSegments[pathSegments.length - 1];
        if (scheduleId) {
            fetchOpenJobList();
            fetchScheduleEdit(scheduleId);
        }

        $("#scheduleEditForm").on("submit", function (event) {
            event.preventDefault();
            const interviewersSelectTag = $(".interviewer-select")[0];
            let selectedInterviewers = [];
            if (interviewersSelectTag.length > 0) {
                selectedInterviewers = Array.from(interviewersSelectTag.options).filter(option => option.selected).map(option => option.value);
            }
            const formData = {
                scheduleId: scheduleId,
                scheduleTitle: $("#scheduleTitle").val(),
                candidateId: $("#jobSelect").val(),
                jobId: $("#candidateId").val(),
                interviewersIdList: interviewersSelectTag.length > 0 ? selectedInterviewers.join(',') : null,
                scheduleDate: $("#scheduleDate").val(),
                scheduleFrom: $("#scheduleFrom").val(),
                scheduleTo: $("#scheduleTo").val(),
                location: $("#location").val(),
                recruiterOwner: $("#scheduleRecruiterOwnerEdit").val(),
                meetingId: $("#meetingId").val(),
                notes: $("#notes").val(),
                result: $("#result").val(),
                status: $("#scheduleStatus").text()
            }

            $.ajax({
                url: `/api/interview-schedules/${scheduleId}`,
                type: 'PUT',
                contentType: 'application/json',
                data: JSON.stringify(formData),
                success: function (data) {
                    $("#resultMessage").text(data);
                    $("#resultMessage").addClass("text-success");
                    $(".invalid-input-user").text("");
                },
                error: function (error) {
                    if (error.status === 400) {
                        const errors = error.responseJSON;
                        $(".invalid-input-user").text("");

                        for (const key in errors) {
                            const errorMessage = errors[key];
                            $(`#${key}`).closest("div").find(".invalid-input-user").text(errorMessage);
                        }
                    } else {
                        $("#resultMessage").text("Created failed. Please try again.");
                        $("#resultMessage").addClass("text-danger");
                    }
                }
            })
        })

        $("#cancelInterviewOkBtn").on("click", function (event) {
            event.preventDefault();

            $.ajax({
                url: `/api/interview-schedules/cancel`,
                type: 'POST',
                data: {
                    scheduleId: scheduleId
                },
                success: function (data) {
                    $("#cancelInterviewConfirmationModal").modal("hide");
                    $("#resultMessage").text("Interview Schedule cancelled successfully!");
                    $("#resultMessage").addClass("text-success");
                    if (data === 'Cancelled') {
                        $('#cancelInterviewButton').addClass('hidden');
                        $("#scheduleStatus").text(data);
                    } else {
                        $('#cancelInterviewButton').removeClass('hidden');
                        $("#scheduleStatus").text(data);
                    }
                },
                error: function (error) {
                    console.log(error);
                }
            })
        })

    }
    function fetchScheduleEdit(scheduleId) {
        $.ajax({
            url: `/api/interview-schedules/edit/${scheduleId}`,
            type: 'GET',
            success: function (data) {
                fetchOpenCandidateListForEdit(data.candidateId, function() {
                    renderScheduleEdit(data);
                    if ($("select#candidateId").length > 0) {
                        $("select#candidateId").val(String(data.candidateId));
                    }
                });
            },
            error: function (error) {
                console.log(error);
            }
        })
    }
    function renderScheduleEdit(schedule) {
        if ($("input#scheduleTitle").length > 0) {
            $("input#scheduleTitle").val(schedule.scheduleTitle);
        }
        if ($("select#jobSelect").length > 0) {
            $("select#jobSelect").val(schedule.jobId);
        }
        if ($("input#scheduleDate").length > 0) {
            $("input#scheduleDate").val(schedule.scheduleDate);
        }
        if ($("input#location").length > 0) {
            $("input#location").val(schedule.location);
        }
        if ($("#scheduleFromValue").length > 0) {
            $("#scheduleFromValue").text(schedule.scheduleFrom);
        }
        if ($("#scheduleToValue").length > 0) {
            $("#scheduleToValue").text(schedule.scheduleTo);
        }
        if ($("input#meetingId").length > 0) {
            $("input#meetingId").val(schedule.meetingId);
        }
        if ($("select#result").length > 0) {
            $("select#result").val(schedule.result);
        }
        if ($("textarea#notes").length > 0) {
            $("textarea#notes").val(schedule.notes);
        }
        $("#scheduleStatus").text(schedule.status);

        if (schedule.status === 'Cancelled') {
            $('#cancelInterviewButton').addClass('hidden');
        } else {
            $('#cancelInterviewButton').removeClass('hidden');
        }

        const interviewersSelectTag = $(".interviewer-select");
        if (interviewersSelectTag) {
            fetchInterviewerList();
        }
        let multipleInterviewerSelect;
        if (interviewersSelectTag) {
            multipleInterviewerSelect = new Choices(interviewersSelectTag[0], {
                removeItemButton: true,
                maxItemCount: 5,
                searchResultLimit: 5,
                renderChoiceLimit: 5
            });
        }
        var selectedInterviewers = schedule.interviewersIdList.split(',').map(interviewer => interviewer.trim());
        if (selectedInterviewers && Array.isArray(selectedInterviewers)) {
            selectedInterviewers.forEach(interviewer => {
                multipleInterviewerSelect.setChoiceByValue(interviewer);
            });
        }
        const scheduleRecruiterOwnerEdit = $("#scheduleRecruiterOwnerEdit");
        if (scheduleRecruiterOwnerEdit.length > 0) {
            $.ajax({
                url: '/api/users/getRecruiters',
                type: 'GET',
                success: function (data) {
                    renderRecruiterList(data);
                    $("select#scheduleRecruiterOwnerEdit").val(schedule.recruiterOwner);
                },
                error: function (error) {
                    console.error('Error fetching recruiters:', error);
                }
            });

            function renderRecruiterList(data) {
                scheduleRecruiterOwnerEdit.empty();
                scheduleRecruiterOwnerEdit.append(
                    '<option value="">Recruiter name</option>'
                );
                data.forEach(function (recruiter) {
                    scheduleRecruiterOwnerEdit.append(
                        `<option value="${recruiter.userId}">${recruiter.fullName}</option>`
                    );
                });
            }
        }

        const scheduleFromInput = $("#scheduleFromValue");
        const scheduleToInput = $("#scheduleToValue");
        const scheduleFromValue = scheduleFromInput.text();
        const scheduleToValue = scheduleToInput.text();
        flatpickr("#scheduleFrom", {
            enableTime: true,
            noCalendar: true,
            dateFormat: "H:i",
            time_24hr: true,
            defaultDate: scheduleFromValue
        });
        flatpickr("#scheduleTo", {
            enableTime: true,
            noCalendar: true,
            dateFormat: "H:i",
            time_24hr: true,
            defaultDate: scheduleToValue
        });
    }
    /* END Schedule Edit */

    /* ------------------------------ */

    /* Schedule Create */
    if (pathSegments.includes("interview-schedules") && pathSegments.includes("create")) {
        fetchOpenJobList();
        fetchOpenCandidateList();
        const interviewersSelectTag = $(".interviewer-select");
        if (interviewersSelectTag) {
            fetchInterviewerList();
        }
        let multipleInterviewerSelect;
        if (interviewersSelectTag) {
            multipleInterviewerSelect = new Choices(interviewersSelectTag[0], {
                removeItemButton: true,
                maxItemCount: 5,
                searchResultLimit: 5,
                renderChoiceLimit: 5
            });
        }

        flatpickr("#scheduleFrom", {
            enableTime: true,
            noCalendar: true,
            dateFormat: "H:i",
            time_24hr: true
        });
        flatpickr("#scheduleTo", {
            enableTime: true,
            noCalendar: true,
            dateFormat: "H:i",
            time_24hr: true
        });
    }
    /* END Schedule Create */

    /* ------------------------------ */


























    function updatePagination(data) {
        currentPage = data.number;
        totalPages = data.totalPages;

        $("#pageInfo").text(`Page ${currentPage + 1} of ${totalPages}`);
        $('#prevPage').prop('disabled', currentPage === 0);
        $('#nextPage').prop('disabled', currentPage + 1 >= totalPages);
    }

    function handlePagination(fetchFunction) {
        $("#prevPage").off("click").on("click", function () {
            if (currentPage > 0) {
                currentPage--;
                fetchFunction(currentPage);
            }
        });

        $("#nextPage").off("click").on("click", function () {
            if (currentPage + 1 < totalPages) {
                currentPage++;
                fetchFunction(currentPage);
            }
        });
    }


});

function formatTime(time) {
    if (!time) return '';
    const [hour, minute] = time.split(':');
    return `${hour}:${minute}`;
}


function viewUser(id) {
    window.location.href = `/users/detail/${id}`;
}

function editUser(id) {
    window.location.href = `/users/edit/${id}`;
}