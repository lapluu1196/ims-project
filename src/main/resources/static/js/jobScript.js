window.onload = function () {
    const skillsSelectTag = document.getElementById('skillsSelectTag');
    const benefitsSelectTag = document.getElementById('benefitsSelectTag');
    const levelsSelectTag = document.getElementById('levelsSelectTag');

    const multipleSkillSelect = new Choices(skillsSelectTag, {
        removeItemButton: true,
        maxItemCount: 5,
        searchResultLimit: 5,
        renderChoiceLimit: 5
    });

    const multipleBenefitsSelect = new Choices(benefitsSelectTag, {
        removeItemButton: true,
        maxItemCount: 5,
        searchResultLimit: 5,
        renderChoiceLimit: 5
    });

    const multipleLevelsSelect = new Choices(levelsSelectTag, {
        removeItemButton: true,
        maxItemCount: 5,
        searchResultLimit: 5,
        renderChoiceLimit: 5
    });
}