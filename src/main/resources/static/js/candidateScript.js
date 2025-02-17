window.onload = function () {
    const skillsSelectTag = document.getElementById('skillsSelectTag');

    const multipleCancelButton = new Choices(skillsSelectTag, {
        removeItemButton: true,
        maxItemCount: 5,
        searchResultLimit: 5,
        renderChoiceLimit: 5
    });
}